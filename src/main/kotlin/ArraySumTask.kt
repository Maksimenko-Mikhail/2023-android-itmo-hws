import java.util.concurrent.CountDownLatch

class ArraySumTask(val list: List<Int>, val countDownLatch: CountDownLatch, val resList: MutableList<Long>) : Runnable {
    override fun run() {
        var sum : Long = 0
        while (countDownLatch.count.toInt() > 0) {
            sum += list.get(countDownLatch.count.toInt() - 1)
            countDownLatch.countDown()
        }
        resList.add(sum)
    }
}