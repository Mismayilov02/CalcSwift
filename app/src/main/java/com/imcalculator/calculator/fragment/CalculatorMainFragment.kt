package com.imcalculator.calculator.fragment
import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.Typeface
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.imcalculator.calculator.R
import com.imcalculator.calculator.adapter.MathRecyclerAdapter
import com.imcalculator.calculator.databinding.FragmentCalculatorMainBinding
import com.imcalculator.calculator.utilty.isCharOperator
import com.imcalculator.calculator.managers.SharedPreferencesManager
import com.imcalculator.calculator.view.CustomButton
import com.imcalculator.calculator.viewmodel.CalculationViewModel


class CalculatorMainFragment : Fragment() {

    lateinit var binding: FragmentCalculatorMainBinding
    lateinit var mathRecyclerAdapter: MathRecyclerAdapter
    lateinit var sharedPreferences: SharedPreferencesManager
    val mathList = mutableListOf("0")
    lateinit var viewModel: CalculationViewModel
    var isEqualsClicked = false
    lateinit var operationButtonList: List<CustomButton>
    lateinit var numberButtonList: List<CustomButton>
    var isEditable = false
    var editPosition = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCalculatorMainBinding.inflate(layoutInflater)
        sharedPreferences = SharedPreferencesManager(requireContext())
        checkTheme()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(CalculationViewModel::class.java)
        sharedPreferences = SharedPreferencesManager(requireContext())
        initViews()
        initRecycler()
        initOnClicks()

        binding.sunSelectBtn.setOnClickListener {
            sharedPreferences.setValue("theme", "light")
           checkTheme()
        }
        binding.moonSelectBtn.setOnClickListener {
            sharedPreferences.setValue("theme", "dark")
            checkTheme()
        }
    }

    private fun checkTheme() {
        val isNightMode = sharedPreferences.getValue("theme", "light") == "dark"

        binding.sunCard.visibility = if (isNightMode) View.GONE else View.VISIBLE
        binding.moonCard.visibility = if (isNightMode) View.VISIBLE else View.GONE
        if (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK != if (isNightMode) Configuration.UI_MODE_NIGHT_YES else Configuration.UI_MODE_NIGHT_NO) {
            AppCompatDelegate.setDefaultNightMode(if (isNightMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun initViews() {
        viewModel.result.observe(viewLifecycleOwner) { result ->
            result?.let {
                binding.resultText.apply {
                    setText(it)
                    movementMethod = ScrollingMovementMethod()
                    setHorizontallyScrolling(true)
                    if (!isEqualsClicked) {
                        textSize = when {
                            it.length > 8 && it.length < 15 -> 33f
                            it.length >= 15 -> 28f
                            else -> 38f
                        }
                    }
                }
            }
        }
    }


    private fun initOnClicks() {
        setupNumberButtons()
        setupOperationButtons()
        setupSpecialButtons()
    }

    private fun setupNumberButtons() {
       numberButtonList = listOf(
            binding.btn0,
            binding.btn00,
            binding.btn1,
            binding.btn2,
            binding.btn3,
            binding.btn4,
            binding.btn5,
            binding.btn6,
            binding.btn7,
            binding.btn8,
            binding.btn9
        )
        numberButtonList.forEach { button ->
            button.setOnClickListener {
                numberClick(button.getButtonText())
            }
        }
    }

    private fun setupOperationButtons() {
        operationButtonList = listOf(
            binding.btnPercent,
            binding.btnDivide,
            binding.btnMultiply,
            binding.btnMinus,
            binding.btnPlus
        )
        operationButtonList.forEach { button ->
            button.setOnClickListener {
                operationClick(button.getButtonText())
            }
        }
    }

    private fun setupSpecialButtons() {
        binding.btnDot.setOnClickListener {
            dotClick()
        }
        binding.btnDelete.setOnClickListener {
            deleteClick()
        }
        binding.btnEqual.setOnClickListener {
            if (isEditable) updateSelectedItem(null, 0)
            else{
            isEqualsClicked = true
            binding.resultText.setTypeface(binding.resultText.getTypeface(), Typeface.BOLD)}
            updateData()
        }
        binding.btnClear.setOnClickListener {
            clearClick()
        }
    }

    private fun dotClick() {
        if (mathList.size == 1 && mathList[0] == "0") {
            mathList[0] = "0."
        } else if ((mathList.last().get(mathList.last().length - 1)).isCharOperator()) {
            mathList.add("0.")
        } else {
            if (!mathList.last().contains(".")) {
                mathList[mathList.size - 1] = mathList.last() + "."
            }
        }
        updateData()
    }

    private fun initRecycler() {
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        mathRecyclerAdapter = MathRecyclerAdapter(requireContext(), mathList, ::updateSelectedItem)
        binding.mathRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = FlexboxLayoutManager(context).apply {
                justifyContent = JustifyContent.FLEX_END
                alignItems = AlignItems.FLEX_END
                flexDirection = FlexDirection.ROW_REVERSE
                flexWrap = FlexWrap.WRAP
            }
            adapter = mathRecyclerAdapter
            layoutDirection = View.LAYOUT_DIRECTION_RTL
            scrollToPosition(mathList.size - 1)
        }
    }

    private fun operationClick(operation: String) {
        if (isEditable){
            mathList[editPosition] = operation
            updateDataByPosition(operation)
            return
        }
        if (isEqualsClicked) {
            val last = binding.resultText.text.toString()
            mathList.clear()
            if (last.toDouble()<0) {
                mathList.add("0")
                mathList.add("-")
                mathList.add(last.substring(1))
            }
            else mathList.add(last)
            mathList.add(operation)
            isEqualsClicked = false
            binding.resultText.setTypeface(binding.resultText.getTypeface(), Typeface.NORMAL);

        } else if (mathList.size > 1) {
            if ((mathList.last().get(mathList.last().length - 1)).isCharOperator()) {
                mathList[mathList.size - 1] = operation
            } else {
                mathList.add(operation)
            }
        } else {
            mathList.add(operation)
        }
        updateData()
    }

    private fun numberClick(number: String) {
        if (isEditable) {
            if (mathList[editPosition] == "0") {
                mathList[editPosition] = number
            } else {
                mathList[editPosition] = mathList[editPosition] + number
            }
            updateDataByPosition(mathList[editPosition])
            return
        }
        if (isEqualsClicked) clearClick()
        if (mathList.size == 1 && mathList[0] == "0" && number != "00" && number != "0") {
            mathList[0] = number
        } else if ((mathList.last().get(mathList.last().length - 1)).isCharOperator()) {
            if (number == "0" || number == "00") return
            mathList.add(number)
        } else {
            if (mathList.last() == "0" && (number == "00" || number == "0" || mathList.last().length > 15)) return
            mathList[mathList.size - 1] = mathList.last() + number
        }
        updateData()
    }

    private fun deleteClick() {
        if (isEditable) {
           if (mathList[editPosition].length > 1) {
               mathList[editPosition] = mathList[editPosition].substring(0, mathList[editPosition].length - 1)
           } else {
               mathList[editPosition] = "0"
           }
            updateDataByPosition(mathList[editPosition])
            return
        }
        if (mathList.size == 1 && mathList[0] == "0") return
        if (mathList.last().length > 1) {
            mathList[mathList.size - 1] = mathList.last().substring(0, mathList.last().length - 1)
        } else {
            if (mathList.size > 1) {
                mathList.removeAt(mathList.size - 1)
            } else {
                mathList[0] = "0"
            }
        }
        updateData()
    }

    private fun clearClick() {
        binding.resultText.setTypeface(null, Typeface.NORMAL)
        isEditable = false
        isEqualsClicked = false
        mathList.clear()
        mathList.add("0")
        updateData()
    }

    fun updateDataByPosition(text: String) {
        mathRecyclerAdapter.updateListByPosition(editPosition, text)
        binding.mathRecyclerView.scrollToPosition(mathList.size - 1)
    }

    private fun updateData() {
        if (isEqualsClicked)binding.resultText.textSize = 42f
        else binding.resultText.textSize = 38f
        viewModel.calculate(mathList.joinToString(""))
        mathRecyclerAdapter.submitList(mathList)
        binding.mathRecyclerView.scrollToPosition(mathList.size - 1)
    }

    @SuppressLint("ResourceType")
    fun updateSelectedItem(isNumber: Boolean?, index: Int) {
        editPosition = index
        isEditable = isNumber!=null
        binding.btnEqual.setButtonText("✓")
        binding.btnClear.isEnabled = false
        binding.btnClear.setButtonTextColor(resources.getColor(R.color.colotHintText))
        binding.btnDelete.isEnabled = true
        binding.btnDelete.setColorFilter(resources.getColor(R.color.colorText))
        when (isNumber) {
            true -> {
                numberButtonList.forEach { it.isEnabled = true
                it.setButtonTextColor(resources.getColor(R.color.colorText))}
                operationButtonList.forEach { it.isEnabled = false
                    it.setButtonTextColor(resources.getColor(R.color.colotHintText)) }
                binding.btnDot.isEnabled = true
                binding.btnDot.setButtonTextColor(resources.getColor(R.color.colorText))
            }
            false -> {
                numberButtonList.forEach { it.isEnabled = false
                    it.setButtonTextColor(resources.getColor(R.color.colotHintText))}
                operationButtonList.forEach { it.isEnabled = true
                    it.setButtonTextColor(resources.getColor(R.color.colorText))}
                binding.btnDelete.isEnabled = false
                binding.btnDelete.setColorFilter(resources.getColor(R.color.colotHintText))
                binding.btnDot.isEnabled = false
                binding.btnDot.setButtonTextColor(resources.getColor(R.color.colotHintText))
            }
            else -> {
                binding.btnEqual.setButtonText("=")
                binding.btnClear.isEnabled = true
                binding.btnClear.setButtonTextColor(resources.getColor(R.color.colorText))
                numberButtonList.forEach { it.isEnabled = true
                    it.setButtonTextColor(resources.getColor(R.color.colorText))}
                operationButtonList.forEach { it.isEnabled = true
                    it.setButtonTextColor(resources.getColor(R.color.colorText))}
                binding.btnDot.isEnabled = true
                binding.btnDot.setButtonTextColor(resources.getColor(R.color.colorText))
            }
        }

    }

}