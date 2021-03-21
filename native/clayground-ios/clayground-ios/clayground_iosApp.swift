//
//  clayground_iosApp.swift
//  clayground-ios
//
//  Created by Ritesh Gupta on 21/03/21.
//

import SwiftUI
import Swinject
import shared

@main
struct clayground_iosApp: App {
	static var appComponent: Resolver!
	init() {
		SharedModule().configure()
		clayground_iosApp.appComponent = Assembler([AppModule()]).resolver
	}

	var body: some Scene {
		WindowGroup {
			MovieDetailView(state: .init())
		}
	}
}

class AppModule: Assembly {
	func assemble(container: Container) {
		container.register(MovieDetailViewModel.self) { _ in
			SharedModule.Companion().movieDetailViewModel
		}
	}
}
