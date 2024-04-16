package com.dev_bayan_ibrahim.flashcards.ui.app.util

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.annotation.StringRes
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.dev_bayan_ibrahim.flashcards.R
import com.dev_bayan_ibrahim.flashcards.data.exception.CardDeserializationException
import com.dev_bayan_ibrahim.flashcards.data.exception.CardDownloadException
import com.dev_bayan_ibrahim.flashcards.data.exception.CardException
import com.dev_bayan_ibrahim.flashcards.data.exception.DeckDeserializationException
import com.dev_bayan_ibrahim.flashcards.data.exception.DeckException
import com.dev_bayan_ibrahim.flashcards.data.exception.DeckNotFoundException
import com.dev_bayan_ibrahim.flashcards.data.exception.OfflineException
import io.ktor.client.plugins.ResponseException
import java.net.UnknownHostException

interface FlashSnackbarVisuals {
    fun asSnackbarVisuals(context: Context): SnackbarVisuals
}

enum class FlashSnackbarMessages(
    @StringRes
    val actionLabel: Int?,
    @StringRes
    val message: Int,
    val duration: SnackbarDuration,
    val withDismissAction: Boolean
) : FlashSnackbarVisuals {

    Success(
        actionLabel = null,
        message = R.string.success,
        duration = SnackbarDuration.Short,
        withDismissAction = false
    ),
    FinishSaveDeck(
        actionLabel = null,
        message = R.string.finish_save_deck,
        duration = SnackbarDuration.Short,
        withDismissAction = false
    ),
    FailDownloadDeck(
        actionLabel = null,
        message = R.string.fail_download_deck,
        duration = SnackbarDuration.Short,
        withDismissAction = false
    ),
    UnknownDeviceRateFailed(
        actionLabel = null,
        message = R.string.unknown_device_rate_failed_hint,
        duration = SnackbarDuration.Short,
        withDismissAction = false
    ),
    RateSuccess(
        actionLabel = null,
        message = R.string.deck_rated_successfully,
        duration = SnackbarDuration.Short,
        withDismissAction = false
    ),
    DuplicateRateError(
        actionLabel = null,
        message = R.string.sorry_you_can_t_rate_the_same_deck_twice,
        duration = SnackbarDuration.Short,
        withDismissAction = false
    ),
    ;

    override fun asSnackbarVisuals(
        context: Context
    ): SnackbarVisuals = FlashSnackbarVisualsImpl(
        actionLabel = actionLabel?.let { context.getString(it) },
        message = context.getString(message),
        duration = duration,
        withDismissAction = withDismissAction
    )

    companion object Factory {

        operator fun invoke(
            throwable: Throwable,
        ): FlashSnackbarVisuals = FlashGeneralSnackbarVisuals(
            actionLabel = { null },
            message = { context -> getThrowableMessage(context, throwable) },
            duration = SnackbarDuration.Long,
            withDismissAction = false,
        )

        operator fun invoke(
            actionLabel: (Context) -> String?,
            message: (Context) -> String,
            duration: SnackbarDuration = SnackbarDuration.Short,
            withDismissAction: Boolean = false,
        ): FlashSnackbarVisuals = FlashGeneralSnackbarVisuals(
            actionLabel = actionLabel,
            message = message,
            duration = duration,
            withDismissAction = withDismissAction
        )
    }
}

private data class FlashGeneralSnackbarVisuals(
    private val actionLabel: (Context) -> String?,
    private val message: (Context) -> String,
    private val duration: SnackbarDuration,
    private val withDismissAction: Boolean,
) : FlashSnackbarVisuals {
    override fun asSnackbarVisuals(context: Context): SnackbarVisuals = FlashSnackbarVisualsImpl(
        actionLabel = actionLabel(context),
        message = message(context),
        duration = duration,
        withDismissAction = withDismissAction
    )
}

private data class FlashSnackbarVisualsImpl(
    override val actionLabel: String?,
    override val message: String,
    override val duration: SnackbarDuration,
    override val withDismissAction: Boolean
) : SnackbarVisuals


@Composable
fun getThrowableMessage(
    throwable: Throwable
): String = getThrowableMessage(
    context = LocalContext.current,
    throwable = throwable
)

fun getThrowableMessage(
    context: Context,
    throwable: Throwable,
    onStatusCode: (ResponseException) -> String? = { null },
    specialHandler: (Throwable) -> String? = { null }
): String = specialHandler(throwable) ?: when (throwable) {
    is CardDeserializationException -> {
        context.getString(R.string.invalid_cards_data)
    }

    is CardDownloadException -> {
        getCodeMessage(context, throwable.code.value)
    }

    is CardException -> {
        context.getString(R.string.invalid_cards_data)
    }

    is DeckDeserializationException -> {
        context.getString(R.string.invalid_deck_data)
    }

    is DeckNotFoundException -> {
        context.getString(R.string.can_not_find_the_required_deck)
    }
    is DeckException -> {
        context.getString(R.string.invalid_deck_data)
    }

    is ResponseException -> {
        val code = throwable.response.status.value
        onStatusCode(throwable) ?: getCodeMessage(context, code)
    }

    OfflineException, is UnknownHostException -> {
        context.getString(R.string.no_internet_connection)
    }

    is SQLiteConstraintException -> {
        context.getString(R.string.problem_in_local_decks_info_can_not_continue_the_actions)
    }
    
    else -> context.getString(R.string.unknown_error)
}

private fun getCodeMessage(
    context: Context,
    code: Int
): String = when (code) {
    200 -> R.string.code_200
    201 -> R.string.code_201
    202 -> R.string.code_202
    203 -> R.string.code_203
    204 -> R.string.code_204
    205 -> R.string.code_205
    206 -> R.string.code_206
    207 -> R.string.code_207

    300 -> R.string.code_300
    301 -> R.string.code_301
    302 -> R.string.code_302
    303 -> R.string.code_303
    304 -> R.string.code_304
    305 -> R.string.code_305
    306 -> R.string.code_306
    307 -> R.string.code_307
    308 -> R.string.code_308

    400 -> R.string.code_400
    401 -> R.string.code_401
    402 -> R.string.code_402
    403 -> R.string.code_403
    404 -> R.string.code_404
    405 -> R.string.code_405
    406 -> R.string.code_406
    407 -> R.string.code_407
    408 -> R.string.code_408
    409 -> R.string.code_409
    410 -> R.string.code_410
    411 -> R.string.code_411
    412 -> R.string.code_412
    413 -> R.string.code_413
    414 -> R.string.code_414
    415 -> R.string.code_415
    416 -> R.string.code_416
    417 -> R.string.code_417
    422 -> R.string.code_422
    423 -> R.string.code_423
    424 -> R.string.code_424
    425 -> R.string.code_425
    426 -> R.string.code_426
    429 -> R.string.code_429
    431 -> R.string.code_431

    500 -> R.string.code_500
    501 -> R.string.code_501
    502 -> R.string.code_502
    503 -> R.string.code_503
    504 -> R.string.code_504
    505 -> R.string.code_505
    506 -> R.string.code_506
    507 -> R.string.code_507
    else -> R.string.unknown_error
}.run {
    context.getString(this)
}
