Pod::Spec.new do |spec|
    spec.name                     = 'Clayground'
    spec.version                  = '0.0.1'
    spec.homepage                 = 'https://github.com/ragdroid/Clayground'
    spec.source                   = { :git => "Not Published" }
    spec.authors                  = 'Garima Jain'
    spec.license                  = 'ragdroid License'
    spec.summary                  = 'Common library for the Clayground iOS app'
    spec.static_framework         = true
    spec.vendored_frameworks      = "build/framework/shared.framework"
    spec.libraries                = "c++"
    spec.module_name              = "#{spec.name}_umbrella"
    spec.platform                 = :ios
    spec.ios.deployment_target    = '14.0'

    spec.pod_target_xcconfig = {
        'KOTLIN_TARGET[sdk=iphonesimulator*]' => 'ios_x64',
        'KOTLIN_TARGET[sdk=iphoneos*]' => 'ios_arm',
        'KOTLIN_TARGET[sdk=macosx*]' => 'macos_x64'
    }

    spec.script_phases = [
        {
            :name => 'Build common',
            :execution_position => :before_compile,
            :shell_path => '/bin/sh',
            :script => <<-SCRIPT
                set -ev
                REPO_ROOT="$PODS_TARGET_SRCROOT"
                "$REPO_ROOT/../gradlew" -p "$REPO_ROOT" :shared:packForXcode \
                    -Pkotlin.native.cocoapods.target=$KOTLIN_TARGET \
                    -Pkotlin.native.cocoapods.configuration=$CONFIGURATION \
                    -Pkotlin.native.cocoapods.cflags="$OTHER_CFLAGS" \
                    -Pkotlin.native.cocoapods.paths.headers="$HEADER_SEARCH_PATHS" \
                    -Pkotlin.native.cocoapods.paths.frameworks="$FRAMEWORK_SEARCH_PATHS"
            SCRIPT
        }
    ]
end
