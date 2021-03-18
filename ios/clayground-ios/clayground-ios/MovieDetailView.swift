	//
//  ContentView.swift
//  clayground-ios
//
//  Created by Ritesh Gupta on 14/03/21.
//

import SwiftUI
import shared

struct MovieDetailView: View {
    
    var movieDetailState: MovieDetailState
    
    var viewModel = NativeViewModel<MovieDetailState, MovieDetailEvent, MovieDetailViewEffect>(viewModel: (clayground_iosApp.appComponent?.resolve(MovieDetailViewModel.self)!)!,
                                            nativeCallback: Collector())
    
    var body: some View {
            if (movieDetailState.loadingState is LoadingState.Idle) {
                Text("Loading")
            } else {
                Text(movieDetailState.movieDetails!.title)
            }
		Text(Greeting().greeting())
            .padding()
            .onAppear {
                viewModel.dispatchEvent(event: MovieDetailEvent.Load())
            }
            .onDisappear() {
                viewModel.onDestroy()
            }
    }
        class Collector: NativeCallback<MovieDetailState, MovieDetailEvent, MovieDetailViewEffect> {
            func handleViewEffects(viewEffect: MovieDetailViewEffect) {
                
            }
            
            func render(state: MovieDetailState) {
                movieDetailState = state
            }
            
        }
    
    
}
struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        MovieDetailView()
    }
}

