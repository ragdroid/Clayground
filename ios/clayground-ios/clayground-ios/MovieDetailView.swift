//
//  MovieDetailView.swift
//  clayground-ios
//
//  Created by Ritesh Gupta on 20/03/21.
//

import SwiftUI
import shared

final class MovieDetailViewState: ObservableObject {
	let collector = Collector()
    lazy var viewModel = NativeViewModel<MovieDetailState, MovieDetailEvent, MovieDetailViewEffect>(viewModel: clayground_iosApp.appComponent!.resolve(MovieDetailViewModel.self)!, nativeCallback: collector)

	func handleOnAppear() {
        viewModel.dispatchEvent(event: MovieDetailEvent.Load())
	}

	func handleOnDisappear() {
//		viewModel.onDestroy()
	}
}

extension MovieDetailViewState {
	final class Collector: NativeCallback<MovieDetailState, MovieDetailEvent, MovieDetailViewEffect> {
		override func handleViewEffects(viewEffect: MovieDetailViewEffect?) {
            PlatformKt.kermitLogger().d(message: "Inside view effects: \(viewEffect!)", tag: "MovieDetailViewState", throwable: nil)
		}
		override func render(state: MovieDetailState?) {
            PlatformKt.kermitLogger().d(message: "Inside render: \(state!)", tag: "MovieDetailViewState", throwable: nil)

		}
	}
    final class NewCollector: Kotlinx_coroutines_coreFlowCollector {
        func emit(value: Any?, completionHandler: @escaping (KotlinUnit?, Error?) -> Void) {
            print("Inside NewCollecttor: \(value)")
        }
        
        
    }
    
}

struct MovieDetailView: View {
	@ObservedObject var state: MovieDetailViewState

	var body: some View {
		Text(Greeting().greeting())
			.padding()
			.onAppear(perform: state.handleOnAppear)
			.onDisappear(perform: state.handleOnDisappear)
	}
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		MovieDetailView(state: .init())
	}
}
