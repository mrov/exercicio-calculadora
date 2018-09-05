package br.ufpe.cin.if710.calculadora

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Digitos
        val btn_0 = findViewById<Button>(R.id.btn_0)
        btn_0.setOnClickListener { text_calc.text.append('0') }

        val btn_1 = findViewById<Button>(R.id.btn_1)
        btn_1.setOnClickListener { text_calc.text.append('1') }

        val btn_2 = findViewById<Button>(R.id.btn_2)
        btn_2.setOnClickListener { text_calc.text.append('2') }

        val btn_3 = findViewById<Button>(R.id.btn_3)
        btn_3.setOnClickListener { text_calc.text.append('3') }

        val btn_4 = findViewById<Button>(R.id.btn_4)
        btn_4.setOnClickListener { text_calc.text.append('4') }

        val btn_5 = findViewById<Button>(R.id.btn_5)
        btn_5.setOnClickListener { text_calc.text.append('5') }

        val btn_6 = findViewById<Button>(R.id.btn_6)
        btn_6.setOnClickListener { text_calc.text.append('6') }

        val btn_7 = findViewById<Button>(R.id.btn_7)
        btn_7.setOnClickListener { text_calc.text.append('7') }

        val btn_8 = findViewById<Button>(R.id.btn_8)
        btn_8.setOnClickListener { text_calc.text.append('8') }

        val btn_9 = findViewById<Button>(R.id.btn_9)
        btn_9.setOnClickListener { text_calc.text.append('9') }

        //Simbolos
        val btn_Divide = findViewById<Button>(R.id.btn_Divide)
        btn_Divide.setOnClickListener { text_calc.text.append('/') }

        val btn_Multiply = findViewById<Button>(R.id.btn_Multiply)
        btn_Multiply.setOnClickListener { text_calc.text.append('*') }

        val btn_Subtract = findViewById<Button>(R.id.btn_Subtract)
        btn_Subtract.setOnClickListener { text_calc.text.append('-') }

        val btn_Add = findViewById<Button>(R.id.btn_Add)
        btn_Add.setOnClickListener { text_calc.text.append('+') }

        val btn_LParen = findViewById<Button>(R.id.btn_LParen)
        btn_LParen.setOnClickListener { text_calc.text.append('(') }

        val btn_RParen = findViewById<Button>(R.id.btn_RParen)
        btn_RParen.setOnClickListener { text_calc.text.append(')') }

        val btn_Dot = findViewById<Button>(R.id.btn_Dot)
        btn_Dot.setOnClickListener { text_calc.text.append('.') }

        //val btn_Equal = findViewById(R.id.btn_Equal) as Button
        //btn_Equal.setOnClickListener { text_calc.text.append('/') }

        //val btn_Power = findViewById(R.id.btn_Power) as Button
        //btn_Power.setOnClickListener { text_calc.text.append('*') }

    }

    //Como usar a função:
    // eval("2+2") == 4.0
    // eval("2+3*4") = 14.0
    // eval("(2+3)*4") = 20.0
    //Fonte: https://stackoverflow.com/a/26227947
    fun eval(str: String): Double {
        return object : Any() {
            var pos = -1
            var ch: Char = ' '
            fun nextChar() {
                val size = str.length
                ch = if ((++pos < size)) str.get(pos) else (-1).toChar()
            }

            fun eat(charToEat: Char): Boolean {
                while (ch == ' ') nextChar()
                if (ch == charToEat) {
                    nextChar()
                    return true
                }
                return false
            }

            fun parse(): Double {
                nextChar()
                val x = parseExpression()
                if (pos < str.length) throw RuntimeException("Caractere inesperado: " + ch)
                return x
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            // | number | functionName factor | factor `^` factor
            fun parseExpression(): Double {
                var x = parseTerm()
                while (true) {
                    if (eat('+'))
                        x += parseTerm() // adição
                    else if (eat('-'))
                        x -= parseTerm() // subtração
                    else
                        return x
                }
            }

            fun parseTerm(): Double {
                var x = parseFactor()
                while (true) {
                    if (eat('*'))
                        x *= parseFactor() // multiplicação
                    else if (eat('/'))
                        x /= parseFactor() // divisão
                    else
                        return x
                }
            }

            fun parseFactor(): Double {
                if (eat('+')) return parseFactor() // + unário
                if (eat('-')) return -parseFactor() // - unário
                var x: Double
                val startPos = this.pos
                if (eat('(')) { // parênteses
                    x = parseExpression()
                    eat(')')
                } else if ((ch in '0'..'9') || ch == '.') { // números
                    while ((ch in '0'..'9') || ch == '.') nextChar()
                    x = java.lang.Double.parseDouble(str.substring(startPos, this.pos))
                } else if (ch in 'a'..'z') { // funções
                    while (ch in 'a'..'z') nextChar()
                    val func = str.substring(startPos, this.pos)
                    x = parseFactor()
                    if (func == "sqrt")
                        x = Math.sqrt(x)
                    else if (func == "sin")
                        x = Math.sin(Math.toRadians(x))
                    else if (func == "cos")
                        x = Math.cos(Math.toRadians(x))
                    else if (func == "tan")
                        x = Math.tan(Math.toRadians(x))
                    else
                        throw RuntimeException("Função desconhecida: " + func)
                } else {
                    throw RuntimeException("Caractere inesperado: " + ch.toChar())
                }
                if (eat('^')) x = Math.pow(x, parseFactor()) // potência
                return x
            }
        }.parse()
    }
}
