import shared
import SwiftUI
import Swinject

class AppModule: Assembly {
	func assemble(container: Container) {
		container.register(MovieDetailViewModel.self) { _ in
			SharedModule.Companion().movieDetailViewModel
		}
	}
}

final class AppModel {
	static let shared = AppModel()
	let appComponent: Resolver = Assembler([AppModule()]).resolver

	init() {
		SharedModule().configure()
	}
}

public final class MovieDetailViewState: ObservableObject {
	@Published public var name: String = "Initial movie name"
	@Published public var isLoading: Bool = true

	public init() {}

	private lazy var viewModel = NativeViewModel<MovieDetailState, MovieDetailEvent, MovieDetailViewEffect>(viewModel: kmpViewModel, render: handleRender, viewEffectHandler: handleViewEffect)

	private lazy var kmpViewModel = AppModel.shared.appComponent.resolve(MovieDetailViewModel.self)!

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

public struct RootView: View {
	public init() {}

	public var body: some View {
		MovieDetailView(state: .init())
	}
}
