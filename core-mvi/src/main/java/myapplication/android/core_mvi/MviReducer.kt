package myapplication.android.core_mvi

interface MviReducer<PartialState: MviPartialState, State: MviState> {

    fun reduce(prevState: State, partialState: PartialState): State
}