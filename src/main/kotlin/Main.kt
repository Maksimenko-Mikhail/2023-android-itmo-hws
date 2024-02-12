import java.util.concurrent.ExecutionException
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger

fun runThread() {
    val list : MutableList<Int> = ArrayList()
    for (i in 1..99) {
        list.add(i)
    }
    val thread = Thread(MyRunnable(list))
    thread.start()
}

fun callCallable() {
    val myCallable1 = MyCallable(100, 3)
    val myCallable2 = MyCallable(20000000, 4)
    val executor = Executors.newCachedThreadPool()
    try {
        val res1 = executor.submit(myCallable1)
        val res2 : Future<Int> = executor.submit(myCallable2) // future оставлен, чтобы явно продемонстрировать тип executor.submit
        println(res1.get())
        println(res2.get())
    } catch (e : ExecutionException) {
        println("ExecutionException occurred with message: " + e.message)
    } finally {
        executor.shutdown()
    }
}

fun deadlock() {
    val sync1 = Any()
    val sync2 = Any()
    val thread1 = Thread {
        synchronized(sync1) {
            Thread.sleep(100)
            synchronized(sync2) {
                println("got sync2 access")
            }
        }
    }
    val thread2 = Thread {
        synchronized(sync2) {
            Thread.sleep(100)
            synchronized(sync1) {
                println("got sync1 access")
            }
        }
    }
    thread1.start()
    thread2.start()
}

class MyInt(var a : Int) {

    fun get() : Int {
        return a
    }
    fun increment() {
        ++a
    }
    override fun toString(): String {
        return a.toString()
    }
}

class MyRun(val myInt : MyInt) : Runnable {
    override fun run() {
        while (true) {
            synchronized(myInt) {
                if (myInt.get() >= 10000) return
                myInt.increment()
            }
        }
    }

}

fun synchronizedThreads() {
    val myInt = MyInt(0)
    val runnable = MyRun(myInt)
    val t1 = Thread(runnable)
    val t2 = Thread(runnable)
    val t3 = Thread(runnable)
    t1.start()
    t2.start()
    t3.start()
    t1.join()
    t2.join()
    t3.join()
    println("sync threads: ${myInt.get()}")
}

//@Volatile
var counter = 0
fun unsynchronizedThreads() {

    val myInt = MyInt(0)
    val runnable = Runnable { while (myInt.get() < 10000) { myInt.increment() } }
    val t1 = Thread(runnable)
    val t2 = Thread(runnable)
    val t3 = Thread(runnable)
    t1.start()
    t2.start()
    t3.start()
    t1.join()
    t2.join()
    t3.join()
    println("unsync threads: $myInt") // иногда выдает отличный от 10000 результат
}

fun main(args: Array<String>) {
    println("Hello threads")
    runThread()
    callCallable()
    synchronizedThreads()
    unsynchronizedThreads()
//    deadlock()
}