package com.nextory.testapp.ui.utils

import java.util.concurrent.CancellationException


fun Exception.rethrowCancellation() {
    if (this is CancellationException) throw this
}