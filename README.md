# Plisio Android SDK

[![JitPack](https://jitpack.io/v/net.plisio/android-sdk.svg)](https://jitpack.io/#net.plisio/android-sdk)

Plisio Android SDK provides a subset of the [Plisio API](https://plisio.net/documentation) to implement payments for [White Label](https://plisio.net/white-label) invoices and customizable prebuilt UI components.

**Plisio Android SDK only covers invoice payments in apps and requires `id` and `viewKey` of an invoice to work with.**

**Use [Plisio API](https://plisio.net/documentation) or one of the [Plisio server libraries](https://plisio.net/integrations) to create invoices and listen for callbacks on your server.**

## Installation

```
repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    // Plisio UI Compose - prebuilt UI made with Jetpack Compose
    implementation("net.plisio.android-sdk:ui-compose:1.0.0")
    // Plisio SDK - API only without a prebuilt UI
    implementation("net.plisio.android-sdk:sdk:1.0.0")
}
```

## Using prebuilt UI

```kotlin
import net.plisio.sdk.ui.compose.*
@Composable
fun Example() {
    val paymentViewModel = plisioPaymentViewModel()
    val paymentStep by paymentViewModel.paymentStep.collectAsState()
    var isPaymentSheetVisible by rememberSaveable { mutableStateOf(false) }

    // Optional, use a regular Button instead if you want to customize it
    PlisioPaymentButton(
        // Used to show a status indicator inside of the button
        paymentStep = paymentStep,
        onClick = {
            // Load an invoice
            paymentViewModel.loadInvoice(id = invoiceID, viewKey = invoiceViewKey)
            // Show the payment sheet
            isPaymentSheetVisible = true
        }
    )

    // The sheet will fill all available space and show as a bottom sheet or a dialog depending on the screen size
    PlisioPaymentSheet(
        isVisible = isPaymentSheetVisible,
        setVisibility = { isPaymentSheetVisible = it },
        paymentStep = paymentStep
    )
}
```

The [demo project](demo/) shows how to use and customize the prebuilt UI.

See `PlisioStyle`, `PlisioPaymentSheet` and `PlisioPaymentButton` code/documentation for all available customization options.

## SDK-only usage

### Using `PlisioPaymentViewModel` to load an invoice and manage its state

#### In Compose

```kotlin
import net.plisio.sdk.ui.compose.*
@Composable
fun Example() {
    val paymentViewModel = plisioPaymentViewModel()
    val paymentStep by paymentViewModel.paymentStep.collectAsState()
    // Use paymentStep to make your UI
    Text("Payment step: $paymentStep")
}
```

#### In a regular `Activity`

```kotlin
import net.plisio.sdk.viewmodels.*
class ExampleActivity: ComponentActivity() {
    val paymentViewModel: PlisioPaymentViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Collect the paymentStep flow
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                paymentViewModel.paymentStep.collectLatest { paymentStep ->
                    // Update your UI here
                }
            }
        }
    }
    
    // Create an invoice on your server, get invoice id and viewKey from your server and pass them to loadInvoice()
    fun loadInvoice(id: PlisioInvoiceID, viewKey: String) {
        paymentViewModel.loadInvoice(id, viewKey)
    }
}
```

### Using API directly

```kotlin
suspend fun PlisioClient.getInvoice(id: PlisioInvoiceID, viewKey: String): Result<PlisioInvoiceDetails>
suspend fun PlisioClient.setUserEmail(email: String, id: PlisioInvoiceID, viewKey: String): Result<PlisioInvoiceDetails>
suspend fun PlisioClient.setCurrency(currency: PlisioCryptoCurrencyID, id: PlisioInvoiceID, viewKey: String): Result<PlisioInvoiceDetails>
```

### Configuration

`PlisioClient` options can be set at any time before loading an invoice. Example with an `Application` subclass:

```kotlin
import net.plisio.sdk.PlisioClient
class App: Application() {
    override fun onCreate() {
        super.onCreate()
        // Log Plisio errors, show more error details in the prebuilt UI
        PlisioClient.configure(
            enableLogging = true,
            showErrorDetails = true
        )
    }
}
```
