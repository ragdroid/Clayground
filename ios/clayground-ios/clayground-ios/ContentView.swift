	//
//  ContentView.swift
//  clayground-ios
//
//  Created by Ritesh Gupta on 14/03/21.
//

import SwiftUI
import shared

struct ContentView: View {
    
    var viewModel: MovieDetailsNativeViewModel!
    var body: some View {
		Text(Greeting().greeting())
            .padding()
    }
    init() {
        class Collector: NativeCallback {
            func handleViewEffects(viewEffect: Any?) {
                
            }
            
            func render(state: Any?) {
                
            }
            
            
        }
        viewModel = MovieDetailsNativeViewModel(viewModel: (clayground_iosApp.appComponent?.resolve(MovieDetailViewModel.self)!)!,
                                                nativeCallback: Collector())
        
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}

