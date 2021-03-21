//
//  AppDelegate.swift
//  clayground-mac
//
//  Created by Ritesh Gupta on 21/03/21.
//

import Cocoa
import SwiftUI
import Swinject
import shared

@main
class AppDelegate: NSObject, NSApplicationDelegate {

	var window: NSWindow!
	static var appComponent: Resolver!

	func applicationDidFinishLaunching(_ aNotification: Notification) {
		SharedModule().configure()
		AppDelegate.appComponent = Assembler([AppModule()]).resolver

		// Create the SwiftUI view that provides the window contents.
		let contentView = MovieDetailView(state: .init())

		// Create the window and set the content view.
		window = NSWindow(
		    contentRect: NSRect(x: 0, y: 0, width: 480, height: 300),
		    styleMask: [.titled, .closable, .miniaturizable, .resizable, .fullSizeContentView],
		    backing: .buffered, defer: false)
		window.isReleasedWhenClosed = false
		window.center()
		window.setFrameAutosaveName("Main Window")
		window.contentView = NSHostingView(rootView: contentView)
		window.makeKeyAndOrderFront(nil)
	}

	func applicationWillTerminate(_ aNotification: Notification) {
		// Insert code here to tear down your application
	}
}

class AppModule: Assembly {
	func assemble(container: Container) {
		container.register(MovieDetailViewModel.self) { _ in
			SharedModule.Companion().movieDetailViewModel
		}
	}
}
