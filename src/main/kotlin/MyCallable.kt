import java.util.concurrent.Callable
import kotlin.jvm.Throws
import java.lang.Math
class MyCallable(val a : Int, val n : Int) : Callable<Int> {

    @Throws(ArithmeticException::class)
    fun binPow(a : Int, n : Int) : Int {
        if (n == 1) return a
        if (n % 2 == 1) return Math.multiplyExact(a, binPow(a, n - 1))
        val res = binPow(a, n / 2)
        return Math.multiplyExact(res, res)
    }

    @Throws(ArithmeticException::class)
    override fun call(): Int {
        return binPow(a, n)
    }

}