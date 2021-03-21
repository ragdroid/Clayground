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
struct ClaygroundApp: App {
    
	private static var appComponent: Resolver!
    
    static func inject<Service>(_ serviceType: Service.Type) throws -> Service? {
        let service = appComponent.resolve(serviceType)
        guard service != nil else {
            throw AppErrors.InjectionError(name: "You must add a provider for \(serviceType)")
        }
        return service
    }
    
	init() {
		SharedModule().configure()
		ClaygroundApp.appComponent = Assembler([AppModule()]).resolver
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

enum AppErrors: Error {
    case InjectionError(name: String)
}
