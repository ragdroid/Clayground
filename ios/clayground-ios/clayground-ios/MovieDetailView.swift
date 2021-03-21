//
//  MovieDetailView.swift
//  clayground-ios
//
//  Created by Ritesh Gupta on 20/03/21.
//

import SwiftUI
import shared

final class MovieDetailViewState: ObservableObject {
	@Published var name: String = "Initial movie name"
	@Published var isLoading: Bool = true

	lazy var viewModel = NativeViewModel<MovieDetailState, MovieDetailEvent, MovieDetailViewEffect>(viewModel: kmpViewModel, render: handleRender, viewEffectHandler: handleViewEffect)

	private lazy var kmpViewModel = clayground_iosApp.appComponent!.resolve(MovieDetailViewModel.self)!

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

struct MovieDetailView: View {
	@ObservedObject var state: MovieDetailViewState

	var body: some View {
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

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		MovieDetailView(state: .init())
	}
}

extension View {
	func eraseToAnyView() -> AnyView {
		AnyView(self)
	}
}
