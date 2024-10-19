package com.imcalculator.calculator.utilty

 fun Char.isCharOperator(): Boolean {
    return this == '+' || this == '-' || this == '×' || this == '÷'|| this == '%'
}

 fun Char.isCharDigit(): Boolean {
    return this in '0'..'9'
}

 fun Char.isCharDot(): Boolean {
    return this == '·'
}
fun Double.format():String{
    return if (this % 1 == 0.0) {
        if (this.toString().contains("E")) {
            val split = this.toString().split("E")
            val first = String.format("%.2f", split[0].toDouble())
            val second = split[1].toInt()
            "${first}E$second"
        }else if (this.toString().length > 10 && this.toString().contains('.')) {
            String.format("%.3f", this)
        } else {
            this.toString().substringBefore('.')
        }
    } else {
        if (this.toString().length > 10 && this.toString().contains('.')) {
            String.format("%.3f", this)
        } else {
            this.toString()
        }
    }
}