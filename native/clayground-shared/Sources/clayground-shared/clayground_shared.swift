import shared
import SwiftUI
import Swinject

final class AppAssembler {
	private static let assembler = Assembler()

	static func resolve<S: Injectable>(_ type: S.Type) throws -> S {
		guard let value = assembler.resolver.resolve(S.self) else {
			assembler.apply(assembly: S.assembly())
			guard let value = try? resolve(S.self) else {
				throw AppErrors.InjectionError(name: "Unable to resolve \(S.self)")
			}
			return value
		}
		return value
	}
}

enum Dependency {
	case movieDetail(MovieDetailViewModel.Type)
	case movieState(MovieDetailViewState.Type)
}

extension Dependency: Assembly {
	func assemble(container: Container) {
		switch self {
			case .movieDetail(let type):
				container.register(type) { r in
					type.instance(resolver: r)
				}
			case .movieState(let type):
				container.register(type) { r in
					type.instance(resolver: r)
				}
		}
	}
}

enum AppErrors: Error {
    case InjectionError(name: String)
}

public final class MovieDetailViewState: ObservableObject {
	@Published public var name: String = "Initial movie name"
	@Published public var isLoading: Bool = true
	@Inject private var kmpViewModel: MovieDetailViewModel

	public init() {}

	private lazy var viewModel = NativeViewModel(viewModel: kmpViewModel, render: handleRender, viewEffectHandler: handleViewEffect)

	private func handleRender(_ state: MovieDetailState?) {
		isLoading = state?.loadingState == LoadingState.Loading()
		name = state?.movieDetails?.title ?? "A movie has no name"
	}

	private func handleViewEffect(_ effect: MovieDetailViewEffect?) {
	}

	func handleOnAppear() {
		viewModel.dispatchEvent(event: MovieDetailEvent.Load())
	}

	func handleOnDisappear() {
		viewModel.onDestroy()
	}
}

public struct MovieDetailView: View {
	@ObservedObject var state: MovieDetailViewState

	public init(state: MovieDetailViewState) {
		self.state = state
	}

	public var body: some View {
		contentView()
			.onAppear(perform: state.handleOnAppear)
			.onDisappear(perform: state.handleOnDisappear)
	}
}

private extension MovieDetailView {
	func contentView() -> some View {
		if state.isLoading {
			return loadingView().eraseToAnyView()
		} else {
			return nameView().eraseToAnyView()
		}
	}
	func nameView() -> some View {
		Text(state.name)
			.padding()
	}
	func loadingView() -> some View {
		LoaderView()
	}
}

extension View {
	func eraseToAnyView() -> AnyView {
		AnyView(self)
	}
}

struct RootViewModel {
	init() {
		SharedModule().configure()
	}
}

public struct RootView: View {
	private let viewModel = RootViewModel()
	@Inject private var movieDetailViewState: MovieDetailViewState

	public init() {}

	public var body: some View {
		MovieDetailView(state: movieDetailViewState)
	}
}

struct MovieDetailView_Previews: PreviewProvider {
    static var previews: some View {
        Text("Testing")
    }
}

protocol Injectable {
	static func instance(resolver: Resolver) -> Self
	static func assembly() -> Assembly
}

@propertyWrapper struct Inject<S: Injectable> {
	let wrappedValue = try! AppAssembler.resolve(S.self)
}

extension MovieDetailViewModel: Injectable {
	static func instance(resolver: Resolver) -> Self {
		SharedModule.Companion().movieDetailViewModel as! Self
	}
	static func assembly() -> Assembly {
		Dependency.movieDetail(MovieDetailViewModel.self)
	}
}

extension MovieDetailViewState: Injectable {
	static func instance(resolver: Resolver) -> Self {
		MovieDetailViewState() as! Self
	}
	static func assembly() -> Assembly {
		Dependency.movieState(MovieDetailViewState.self)
	}
}
