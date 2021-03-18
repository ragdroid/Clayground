package com.ragdroid.clayground.shared

import co.touchlab.kermit.Logger

expect class Platform() {
    val platform: String
}
expect fun kermitLogger(): Logger
