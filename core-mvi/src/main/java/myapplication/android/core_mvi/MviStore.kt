package myapplication.android.core_mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.launch


abstract class MviStore<
        PartialState : MviPartialState,
        Intent : MviIntent,
        State : MviState,
        Effect : MviEffect>(
    private val reducer: MviReducer<PartialState, State>,
    private val actor: MviActor<PartialState, Intent, State, Effect>
) : ViewModel() {
    private val initialState: State by lazy { initialStateCreator() }

    abstract fun initialStateCreator(): State

    private val _uiState: MutableStateFlow<State> = MutableStateFlow(initialState)
    val uiState: StateFlow<State> = _uiState.asStateFlow()

    private val _intents: MutableSharedFlow<Intent> = MutableSharedFlow()
    private val intents: SharedFlow<Intent> = _intents.asSharedFlow()

    private val _effects: MutableSharedFlow<Effect> = MutableSharedFlow()
    val effect: SharedFlow<Effect> = _effects.asSharedFlow()

    init {
        subscribe()
    }

    private fun subscribe() {
        intents
            .flatMapConcat { actor.resolve(it, _uiState.value) }
            .scan(initialState, reducer::reduce)
            .onEach { _uiState.emit(it) }
            .launchIn(viewModelScope)
        actor.effects.onEach { _effects.emit(it) }.launchIn(viewModelScope)
    }

    fun sendEffect(effect: Effect){
        viewModelScope.launch { _effects.emit(effect) }
    }

    fun sendIntent(intent: Intent){
        viewModelScope.launch { _intents.emit(intent) }
    }
}