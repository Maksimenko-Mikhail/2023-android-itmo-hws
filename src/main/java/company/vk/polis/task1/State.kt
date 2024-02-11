package company.vk.polis.task1

sealed interface State {
    class READ : State
    class UNREAD : State
    class DELETED(val userId : Int) : State
}