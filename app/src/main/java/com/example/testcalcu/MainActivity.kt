package com.example.testcalcu

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.testcalcu.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var currentInput = ""
    private var previousInput = ""
    private var operator = ""
    private var hasResult = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupButtons()
    }

    private fun setupButtons() {
        binding.btn0.setOnClickListener { appendDigit("0") }
        binding.btn1.setOnClickListener { appendDigit("1") }
        binding.btn2.setOnClickListener { appendDigit("2") }
        binding.btn3.setOnClickListener { appendDigit("3") }
        binding.btn4.setOnClickListener { appendDigit("4") }
        binding.btn5.setOnClickListener { appendDigit("5") }
        binding.btn6.setOnClickListener { appendDigit("6") }
        binding.btn7.setOnClickListener { appendDigit("7") }
        binding.btn8.setOnClickListener { appendDigit("8") }
        binding.btn9.setOnClickListener { appendDigit("9") }
        binding.btnDot.setOnClickListener { appendDigit(".") }

        binding.btnPlus.setOnClickListener { setOperator("+") }
        binding.btnMinus.setOnClickListener { setOperator("−") }
        binding.btnMultiply.setOnClickListener { setOperator("×") }
        binding.btnDivide.setOnClickListener { setOperator("÷") }

        binding.btnClear.setOnClickListener { clear() }
        binding.btnSign.setOnClickListener { toggleSign() }
        binding.btnPercent.setOnClickListener { percentage() }
        binding.btnBackspace.setOnClickListener { backspace() }
        binding.btnEquals.setOnClickListener { calculate() }
    }

    private fun appendDigit(digit: String) {
        if (hasResult) {
            currentInput = ""
            hasResult = false
        }
        if (digit == "." && currentInput.contains(".")) return
        if (digit == "0" && currentInput == "0") return
        if (digit != "." && currentInput == "0") currentInput = ""
        currentInput += digit
        updateDisplay()
    }

    private fun setOperator(op: String) {
        if (currentInput.isEmpty() && previousInput.isNotEmpty()) {
            operator = op
            updateExpression()
            return
        }
        if (currentInput.isNotEmpty() && previousInput.isNotEmpty()) {
            calculate(keepGoing = true)
        }
        if (currentInput.isNotEmpty()) {
            previousInput = currentInput
            currentInput = ""
            operator = op
        }
        hasResult = false
        updateExpression()
    }

    private fun calculate(keepGoing: Boolean = false) {
        if (previousInput.isEmpty() || currentInput.isEmpty() || operator.isEmpty()) return
        val a = previousInput.toDoubleOrNull() ?: return
        val b = currentInput.toDoubleOrNull() ?: return
        val result = when (operator) {
            "+" -> a + b
            "−" -> a - b
            "×" -> a * b
            "÷" -> {
                if (b == 0.0) { showError(); return }
                a / b
            }
            else -> return
        }
        val formatted = formatResult(result)
        if (keepGoing) {
            previousInput = formatted
            currentInput = ""
        } else {
            binding.tvExpression.text = "$previousInput $operator $currentInput ="
            previousInput = ""
            currentInput = formatted
            operator = ""
            hasResult = true
        }
        updateDisplay()
    }

    private fun clear() {
        currentInput = ""
        previousInput = ""
        operator = ""
        hasResult = false
        binding.tvExpression.text = ""
        binding.tvResult.text = "0"
    }

    private fun backspace() {
        if (hasResult || currentInput.isEmpty()) return
        currentInput = currentInput.dropLast(1)
        updateDisplay()
    }

    private fun toggleSign() {
        if (currentInput.isEmpty() || currentInput == "0") return
        currentInput = if (currentInput.startsWith("-")) currentInput.drop(1)
        else "-$currentInput"
        updateDisplay()
    }

    private fun percentage() {
        val num = currentInput.toDoubleOrNull() ?: return
        currentInput = formatResult(num / 100)
        updateDisplay()
    }

    private fun showError() {
        binding.tvExpression.text = ""
        binding.tvResult.text = "Error"
        currentInput = ""
        previousInput = ""
        operator = ""
        hasResult = true
    }

    private fun formatResult(value: Double): String {
        return if (value == value.toLong().toDouble()) {
            value.toLong().toString()
        } else {
            value.toBigDecimal().stripTrailingZeros().toPlainString()
        }
    }

    private fun updateDisplay() {
        val display = when {
            currentInput.isNotEmpty() -> currentInput
            previousInput.isNotEmpty() -> previousInput
            else -> "0"
        }
        binding.tvResult.text = display
        if (!hasResult) updateExpression()
    }

    private fun updateExpression() {
        binding.tvExpression.text =
            if (operator.isNotEmpty()) "$previousInput $operator" else ""
    }
}
