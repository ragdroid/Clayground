// swift-tools-version:5.3
// The swift-tools-version declares the minimum version of Swift required to build this package.

import PackageDescription

let package = Package(
    name: "clayground-shared",
	platforms: [.iOS(.v13), .macOS(.v10_15)],
    products: [
        // Products define the executables and libraries a package produces, and make them visible to other packages.
        .library(
            name: "clayground-shared",
            targets: ["clayground-shared"]),
    ],
    dependencies: [
        // Dependencies declare other packages that this package depends on.
        // .package(url: /* package url */, from: "1.0.0"),
        .package(name: "shared", path: "../../shared/swiftpackage"),
		.package(url: "https://github.com/Swinject/Swinject.git", from: "2.7.1")
    ],
    targets: [
        // Targets are the basic building blocks of a package. A target can define a module or a test suite.
        // Targets can depend on other targets in this package, and on products in packages this package depends on.
        .target(
            name: "clayground-shared",
            dependencies: ["Swinject", "shared"]),
        .testTarget(
            name: "clayground-sharedTests",
            dependencies: ["clayground-shared"]),
    ]
)
