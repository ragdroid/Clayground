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

    spec.prepare_command = <<-SCRIPT
        set -ev
        cd ../
        ./gradlew shared:packForXcode
        cd shared
    SCRIPT
end
