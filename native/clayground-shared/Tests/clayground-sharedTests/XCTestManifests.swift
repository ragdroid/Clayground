import XCTest

#if !canImport(ObjectiveC)
public func allTests() -> [XCTestCaseEntry] {
    return [
        testCase(clayground_sharedTests.allTests),
    ]
}
#endif
