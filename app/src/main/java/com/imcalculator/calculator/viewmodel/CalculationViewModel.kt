package com.imcalculator.calculator.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.imcalculator.calculator.repo.CalculationRepository

class CalculationViewModel: ViewModel() {
    private var calculationRepository = CalculationRepository()
    var result: MutableLiveData<String?> = calculationRepository.getResult()

    fun calculate(expression: String) {
        calculationRepository.calculate(expression)
        calculationRepository.getResult()
    }
    fun clearResult() {
        calculationRepository.clearResult()
    }
}