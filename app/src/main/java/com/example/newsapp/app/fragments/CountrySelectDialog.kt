package com.example.newsapp.app.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.DialogFragment
import com.example.newsapp.R
import com.example.newsapp.viewmodels.newslist.NewsListViewModel

class CountrySelectDialog(private val newsListViewModel: NewsListViewModel) : DialogFragment() {

    companion object{
        const val DIALOG_TAG = "COUNTRY_SELECT_DIALOG_TAG"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view = layoutInflater.inflate(R.layout.dialog_country_select, null)

        //SETTING SPINNER
        val spinner = view.findViewById<Spinner>(R.id.spinner)
        val array = resources.getStringArray(R.array.countryCodes)
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.countryCodes,
            android.R.layout.simple_spinner_item
        )
        spinner.adapter = adapter
        spinner.setSelection(array.indexOf(newsListViewModel.country))

        return AlertDialog.Builder(requireContext())
            .setView(view)
            .setTitle("Change country resource")
            .setPositiveButton(
                "Change"
            ) { _, _ -> newsListViewModel.changeCurrentCountry(spinner.selectedItem.toString()) }
            .create()

    }
}