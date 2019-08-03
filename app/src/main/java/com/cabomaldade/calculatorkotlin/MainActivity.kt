package com.cabomaldade.calculatorkotlin

import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_main.*
import androidx.core.os.HandlerCompat.postDelayed





private const val STATE_PENDING_OPERATION = "PendingOperation"
private const val STATE_OPERAND1 = "Operand"
private const val STATE_OPERAND1_STORED = "Operand1_Stored"

class MainActivity : AppCompatActivity() {

    //-- Local Variables  -- Will be accessed directly with kotlinx
    //private lateinit var result: EditText
    //private lateinit var newNumber: EditText
    //private val displayOperation by lazy(LazyThreadSafetyMode.NONE){ findViewById<TextView>(R.id.result)}


    // Variables for holding operands
    private var operand1: Double? = null
    private var pendingOperation = "="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*
        result = findViewById(R.id.result)  //-- Local Variables
        newNumber = findViewById(R.id.input) //-- Local Variables

        //operations buttons  -- Local Variables
        val button0: Button = findViewById(R.id.btn0)
        val button1: Button = findViewById(R.id.btn1)
        val button2: Button = findViewById(R.id.btn2)
        val button3: Button = findViewById(R.id.btn3)
        val button4: Button = findViewById(R.id.btn4)
        val button5: Button = findViewById(R.id.btn5)
        val button6: Button = findViewById(R.id.btn6)
        val button7: Button = findViewById(R.id.btn7)
        val button8: Button = findViewById(R.id.btn8)
        val button9: Button = findViewById(R.id.btn9)

        // Operation buttons
        val buttonEquals = findViewById<Button>(R.id.equal)
        val buttonDivide = findViewById<Button>(R.id.divide)
        val buttonMultiply = findViewById<Button>(R.id.multiply)
        val buttonMinus = findViewById<Button>(R.id.minus)
        val buttonDot = findViewById<Button>(R.id.btndot)

        */

        // So, with import kotlinx.android.synthetic.main.activity_main.*  we can access the variables direct

        val listOfButtons = listOf(btn0,btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9)
        val listOfOperands = listOf(equal,multiply,divide,minus,add)

        // OOP here, if a new button is added it is prepared to receive a function
        val listenerFromZeroToNine = View.OnClickListener { view ->
            val button = view as Button
            input.append(button.text)
        }

        for (btn in listOfButtons){
            btn.setOnClickListener(listenerFromZeroToNine)
        }

        val operationListener = View.OnClickListener { view ->
            val operation = (view as Button).text.toString()
            try {
                val value = input.text.toString().toDouble()
                performOperation(value, operation)
            } catch (e: NumberFormatException){
                input.setText("")
            }
            pendingOperation = operation
            signal.text = pendingOperation
        }

        for (operands in listOfOperands){
            operands.setOnClickListener((operationListener))
        }

        btndot.setOnClickListener { view ->
            val dot = (view as Button).text.toString()

            val temp = input.text.toString()

            println(dot)

            if (temp.contains(".") || temp.isEmpty()){
                println(" not so great")
                Toast.makeText(this, "Do not do that!", Toast.LENGTH_SHORT).show()
            }
            else {

                //input.setText("") // I guess this is a bug from android, the DOT goes at the beginning, but at the
                                    // console it shows correctly 12. instead of .12 (screen)


                input.setText(temp + dot)

                println(temp + dot)
                println("great")
            }
        }



    }

    private fun performOperation(value: Double, operation: String) {
        if (operand1 == null){
            operand1 = value
        } else {
            if (pendingOperation == "=") {
                pendingOperation = operation
            }

            when (pendingOperation) {
                "=" -> operand1 = value
                "/" -> operand1 = if (value == 0.0) { // prohibited division
                                    Double.NaN
                                } else {
                                    operand1!! / value
                                }
                "*" -> operand1 = operand1!! * value
                "-" -> operand1 = operand1!! - value
                "+" -> operand1 = operand1!! + value
            }
        }
        result.setText(operand1.toString())
        input.setText("")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (operand1 != null) {
            outState.putDouble(STATE_OPERAND1, operand1!!)
            outState.putBoolean(STATE_OPERAND1_STORED, true)
        }
        outState.putString(STATE_PENDING_OPERATION, pendingOperation)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        operand1 = if (savedInstanceState.getBoolean(STATE_OPERAND1_STORED, false)){
                        savedInstanceState.getDouble(STATE_OPERAND1)
                    } else {
                        null
                    }
        pendingOperation = savedInstanceState.getString(STATE_PENDING_OPERATION)!!
        result.text = pendingOperation
    }

}
