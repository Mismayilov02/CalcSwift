package com.imcalculator.calculator.repo

import androidx.lifecycle.MutableLiveData
import com.imcalculator.calculator.utilty.format
import com.imcalculator.calculator.utilty.isCharOperator

class CalculationRepository {
    private var result:MutableLiveData<String?> = MutableLiveData()

    fun calculate(expression: String) {
        if (isExpressionValid(expression)) result.value = evaluateExpression(expression).format()
    }

    fun getResult(): MutableLiveData<String?> {
        return result
    }


    fun clearResult() {
        result.value = ""
    }


    private fun evaluateExpression(expression: String): Double {
        val operators = listOf('×', '÷', '%', '+', '-')

        var currentNumber = StringBuilder()
        val numbers = mutableListOf<Double>()
        val ops = mutableListOf<Char>()

        for (char in expression) {
            if (char.isDigit() || char == '.' || char == ',' || char == 'E' || (char == '-' && currentNumber.isEmpty())) {
                currentNumber.append(char)
            } else {
                if (currentNumber.isNotEmpty()) {
                    val number = currentNumber.toString().replace(',', '.').toDouble()
                    numbers.add(number)
                    currentNumber = StringBuilder()
                }
                if (char in operators) {
                    while (ops.isNotEmpty() && precedence(ops.last()) >= precedence(char)) {
                        val op = ops.removeLast()
                        val num2 = numbers.removeLast()
                        val num1 = numbers.removeLast()
                        numbers.add(applyOperator(num1, num2, op))
                    }
                    ops.add(char)
                }
            }
        }

        if (currentNumber.isNotEmpty()) {
            val number = currentNumber.toString().replace(',', '.').toDouble()
            numbers.add(number)
        }

        while (ops.isNotEmpty()) {
            val op = ops.removeLast()
            val num2 = numbers.removeLast()
            val num1 = numbers.removeLast()
            numbers.add(applyOperator(num1, num2, op))
        }

        return numbers.first()
    }

    private fun precedence(operator: Char): Int {
        return when (operator) {
            '×', '÷', '%' -> 2
            '+', '-' -> 1
            else -> throw IllegalArgumentException("Geçersiz operatör: $operator")
        }
    }

    private fun applyOperator(num1: Double, num2: Double, operator: Char): Double {
        return when (operator) {
            '+' -> num1 + num2
            '-' -> num1 - num2
            '×' -> num1 * num2
            '÷' -> num1 / num2
            '%' -> num1 % num2
            else -> throw IllegalArgumentException("Geçersiz operatör: $operator")
        }
    }
    private fun isExpressionValid(expression: String): Boolean {
        if (expression.isEmpty()) {
            return false
        }
        if (expression[0].isCharOperator() || expression[expression.length - 1].isCharOperator()) {
            return false
        }
        return true
    }



}