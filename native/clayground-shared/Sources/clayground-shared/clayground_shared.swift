import shared
import SwiftUI
import Swinject

final class AppAssembler {
	private static let resolver = Assembler(Dependency.allCases).resolver

	static func resolve<S>() throws -> S {
		guard let value = resolver.resolve(S.self) else {
			throw AppErrors.InjectionError(name: "You must add a provider for \(S.self)")
		}
		return value
	}
}

enum Dependency: Assembly, CaseIterable {
	case movieDetail
	case movieState
}

extension Dependency {
	func assemble(container: Container) {
		switch self {
			case .movieDetail:
				container.register(MovieDetailViewModel.self) { _ in
					SharedModule.Companion().movieDetailViewModel
				}
			case .movieState:
				container.register(MovieDetailViewState.self) { _ in
					MovieDetailViewState()
				}
		}
	}
}

@propertyWrapper struct Inject<S> {
	let wrappedValue: S = try! AppAssembler.resolve()
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
