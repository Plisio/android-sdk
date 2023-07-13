package net.plisio.sdk.ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import net.plisio.sdk.viewmodels.PlisioPaymentViewModel

@Composable
fun plisioPaymentViewModel(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
): PlisioPaymentViewModel {
    val vm = viewModel<PlisioPaymentViewModel>()
    lifecycleOwner.lifecycle.addObserver(vm)
    return vm
}