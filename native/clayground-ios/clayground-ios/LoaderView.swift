//
//  LoaderView.swift
//  clayground-ios
//
//  Created by Ritesh Gupta on 21/03/21.
//

import SwiftUI

struct LoaderView: UIViewRepresentable {
	typealias UIViewType = UIActivityIndicatorView
	func makeUIView(context: Context) -> UIActivityIndicatorView {
		let view = UIActivityIndicatorView(style: .large)
		view.color = .systemPink
		view.startAnimating()
		return view
	}
	func updateUIView(_ uiView: UIActivityIndicatorView, context: Context) {}
}
