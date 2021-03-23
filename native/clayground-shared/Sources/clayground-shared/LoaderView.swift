//
//  LoaderView.swift
//  clayground-ios
//
//  Created by Ritesh Gupta on 21/03/21.
//

#if !os(macOS)
import UIKit
#endif
import SwiftUI

#if !os(macOS)
struct UIKitLoaderView: UIViewRepresentable {
	typealias UIViewType = UIActivityIndicatorView
	func makeUIView(context: Context) -> UIActivityIndicatorView {
		let view = UIActivityIndicatorView(style: .large)
		view.color = .systemPink
		view.startAnimating()
		return view
	}
	func updateUIView(_ uiView: UIActivityIndicatorView, context: Context) {}
}
#else
#endif

struct LoaderView: View {
	var body: some View {
		#if os(macOS)
		Text("Loading...")
		#elseif os(iOS)
		UIKitLoaderView()
		#else
		EmptyView()
		#endif
	}
}
