package com.github.funczz.ruby_calc.core.service

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

@Suppress("NonAsciiCharacters")
class RubyServiceTest {

    @Test
    fun healthCheckTest() {
        assertEquals(true, rubyService.healthCheck())
    }

    @Test
    fun helloWorldTest() {
        val expected = "hello world."
        val actual = rubyService.execute(
            code = "ARGV[0].to_s",
            argv = arrayOf(expected)
        )
        assertEquals(expected, actual)
    }

    @Test
    fun blankTest() {
        val expected = ""
        val actual = rubyService.execute(
            code = "''",
            argv = arrayOf()
        )
        assertEquals(expected, actual)
    }

    @Test
    fun sumTest() {
        val expected = "3"
        val actual = rubyService.execute(
            code = "ARGV.reduce(0) {|sum, s| sum + s.to_i}",
            argv = arrayOf("1", "2")
        )
        assertEquals(expected, actual)
    }

    @Test
    fun bigDecimalMathTest() {
        val expected = "1.3958333333333333333333333333333333"
        val actual = rubyService.execute(
            code = "%s\nARGV[0].dms_to_degree.to_p".format(
                RubyService.REQUIRED_BIGDECIMAL_MATH_JRUBY_SCRIPT_HEADER
            ),
            argv = arrayOf("1.2345")
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `Ruby スクリプト実行時にエラーが発生する`() {
        assertThrows(RubyService.EvalException::class.java) {
            rubyService.execute(code = "'1'.foo")
        }
    }

    @Test
    fun `Ruby インスタンスを再利用する - 前回実行時に定義した変数にアクセスできない`() {
        assertEquals("hello world.", rubyService.execute("s='hello world.';s"))
        assertThrows(
            "前回実行した状態は残っていないので、RubyService.EvalExceptionが発生する",
            RubyService.EvalException::class.java
        ) {
            rubyService.execute("s")
        }
    }

    @Test
    fun `Ruby インスタンスを再利用する - 前回実行時の ARGV にアクセスできない`() {
        assertEquals(
            "1",
            rubyService.execute("ARGV.size", arrayOf("hello world."))
        )
        assertEquals(
            "0",
            rubyService.execute("ARGV.size")
        )
    }

    private lateinit var rubyService: RubyService

    @Before
    fun beforeEach() {
        rubyService = RubyService()
    }
}
