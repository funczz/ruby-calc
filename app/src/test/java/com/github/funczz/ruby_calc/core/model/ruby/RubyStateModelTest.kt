package com.github.funczz.ruby_calc.core.model.ruby

import com.github.funczz.ruby_calc.core.service.RubyService
import com.github.funczz.ruby_calc.core.usecase.ruby.ExecuteRubyUseCase
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RubyStateModelTest {

    @Test
    fun executeRuby() {
        val expected = "hello world."
        val inputData = RubyStateData.InputData(
            value = ExecuteRubyUseCase.InputData(
                code = "ARGV[0]",
                argv = listOf(expected)
            )
        )
        model.present(data = inputData)

        assertEquals(false, model.rubyResult.failure.isPresent)
        assertEquals(expected, model.rubyResult.success)
    }

    @Test
    fun clearRubyResult() {
        val expected = RubyResult()
        val inputData = RubyStateData.InputData(
            value = ExecuteRubyUseCase.InputData(
                code = "''",
                argv = listOf(),
            )
        )
        model.present(data = inputData)

        assertEquals(expected, model.rubyResult)
    }

    private lateinit var model: RubyStateModel

    @Before
    fun beforeEach() {
        model = RubyStateModel.new(rubyService = RubyService())
    }

}
