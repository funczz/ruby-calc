package com.github.funczz.ruby_calc.core.service

import org.jruby.embed.ScriptingContainer
import org.jruby.util.KCode
import java.io.Serial

@Suppress("Unused", "MemberVisibilityCanBePrivate")
class RubyService {

    constructor() {
        this.ruby = newRubyInstance()
    }

    constructor(ruby: ScriptingContainer) : this() {
        this.ruby = ruby
    }

    private var ruby: ScriptingContainer

    fun execute(code: String): String {
        return execute(code = code, argv = emptyArray())
    }

    fun execute(code: String, argv: Array<String>): String {
        val result = try {
            ruby.terminate()
            ruby.put("ARGV", argv)
            ruby.runScriptlet(code)
        } catch (th: Throwable) {
            throw EvalException(message = "Failed to evaluate.", cause = th)
        }
        return try {
            result.toString()
        } catch (th: NullPointerException) {
            throw EvalException("The script returned null.", cause = th)
        } catch (th: Throwable) {
            throw EvalException(message = "Failed to convert object to string.", cause = th)
        }
    }

    fun healthCheck(): Boolean {
        return execute("%s1".format(JRUBY_SCRIPT_HEADER)) == "1"
    }

    companion object {

        const val JRUBY_SCRIPT_HEADER = "#encoding:utf-8\n"

        const val REQUIRED_BIGDECIMAL_MATH_JRUBY_SCRIPT_HEADER =
            "#encoding:utf-8\nrequire \"uri:classloader:/jruby/lib/bigdecimal_math.rb\"\n"

        fun newRubyInstance(): ScriptingContainer {
            System.setProperty("jruby.bytecode.version", "1.6")
            return ScriptingContainer().also {
                it.kCode = KCode.UTF8
            }
        }
    }

    class EvalException(message: String, cause: Throwable? = null) : Exception(message, cause) {
        companion object {
            @Serial
            private const val serialVersionUID: Long = -3832420860394008472L
        }
    }
}
