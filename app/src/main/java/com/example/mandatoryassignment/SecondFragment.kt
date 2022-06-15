package com.example.mandatoryassignment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.mandatoryassignment.databinding.FragmentSecondBinding
import com.example.mandatoryassignment.models.ResaleItem
import com.example.mandatoryassignment.models.ResaleItemsViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {
    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val resaleItemsViewModel: ResaleItemsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = requireArguments()
        val secondFragmentArgs: SecondFragmentArgs = SecondFragmentArgs.fromBundle(bundle)
        val position = secondFragmentArgs.position
        val resaleItem = resaleItemsViewModel[position]
        if (resaleItem == null) {
            binding.textviewMessage.text = "No such item!"
            return
        }
        binding.editTextTitle.setText(resaleItem.title)
        binding.editTextDescription.setText(resaleItem.description)
        binding.editTextPrice.setText(resaleItem.price.toString())
        binding.editTextSeller.setText(resaleItem.seller)
        binding.editTextDate.setText(resaleItem.date.toString())
        binding.editTextPictureUrl.setText(resaleItem.pictureUrl)

        binding.buttonBack.setOnClickListener {
            // findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
            // https://stackoverflow.com/questions/60003039/why-android-navigation-component-screen-not-go-back-to-previous-fragment-but-a-m
            findNavController().popBackStack()
        }

        binding.buttonDelete.setOnClickListener {
            if (Firebase.auth.currentUser != null) {
                resaleItemsViewModel.delete(resaleItem.id)
                findNavController().popBackStack()
            } else {
                Snackbar.make(binding.root, "Please sign in to delete item", Snackbar.LENGTH_LONG)
                    .show()
            }
        }

        binding.buttonUpdate.setOnClickListener {
            if (Firebase.auth.currentUser != null) {
                val title = binding.editTextTitle.text.toString().trim()
                val description = binding.editTextDescription.text.toString().trim()
                val price = binding.editTextPrice.text.toString().trim().toInt()
                val seller = binding.editTextSeller.text.toString().trim()
                val date = binding.editTextDate.text.toString().trim().toInt()
                val pictureUrl = binding.editTextPictureUrl.text.toString().trim()
                val updatedItem =
                    ResaleItem(resaleItem.id, title, description, price, seller, date, pictureUrl)
                Log.d("APPLE", "update $updatedItem")
                resaleItemsViewModel.update(updatedItem)
                findNavController().popBackStack()
            } else {
                Snackbar.make(binding.root, "Please sign in to update item", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}