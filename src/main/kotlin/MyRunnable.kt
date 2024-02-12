class MyRunnable(val array: List<Int>) : Runnable {
    override fun run() {
        println(array.sum())
    }
}