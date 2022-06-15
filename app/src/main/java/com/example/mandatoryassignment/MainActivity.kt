package com.example.mandatoryassignment

import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.mandatoryassignment.databinding.ActivityMainBinding
import com.example.mandatoryassignment.models.ResaleItem
import com.example.mandatoryassignment.models.ResaleItemsViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val resaleItemsViewModel: ResaleItemsViewModel by viewModels()
    private var mAuth: FirebaseAuth? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance();
        auth = Firebase.auth

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            if (Firebase.auth.currentUser != null) {
                showDialog()
            } else {
                Snackbar.make(binding.root, "Please sign in to add an item", Snackbar.LENGTH_LONG)
                    .show()
            }
            //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            //.setAction("Action", null).show()
        }

        resaleItemsViewModel.updateMessageLiveData.observe(this) { message ->
            Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            R.id.action_signin -> {
                showLoginDialog()
                true
            }
            R.id.action_signout -> {
                if (Firebase.auth.currentUser != null) {
                    Firebase.auth.signOut()
                    val navController = findNavController(R.id.nav_host_fragment_content_main)
                    navController.popBackStack(R.id.FirstFragment, false)
                    Snackbar.make(binding.root, "Signed out", Snackbar.LENGTH_LONG).show()
                } else {
                    Snackbar.make(binding.root, "Cannot sign out", Snackbar.LENGTH_LONG).show()
                }
                true
            }
            R.id.action_filter -> {
                showFilterDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    private fun showLoginDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Sign In")

        val layout = LinearLayout(this@MainActivity)
        layout.orientation = LinearLayout.VERTICAL

        val emailInputField = EditText(this)
        emailInputField.hint = "Email"
        emailInputField.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        layout.addView(emailInputField)

        val passwordInputField = EditText(this)
        passwordInputField.hint = "Password"
        passwordInputField.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        layout.addView(passwordInputField)

        builder.setView(layout)

        builder.setPositiveButton("Sign Up") { dialog, which ->
            val email = emailInputField.text.toString().trim()
            val password = passwordInputField.text.toString().trim()
            when {
                email.isEmpty() ->
                    //inputField.error = "No word"
                    Snackbar.make(binding.root, "No email", Snackbar.LENGTH_LONG).show()
                password.isEmpty() -> Snackbar.make(
                    binding.root,
                    "No password",
                    Snackbar.LENGTH_LONG
                )
                    .show()
                else -> {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Snackbar.make(binding.root, "User created", Snackbar.LENGTH_LONG)
                                    .show()
                            } else {
                                Snackbar.make(binding.root, "Not created", Snackbar.LENGTH_LONG)
                                    .show()
                            }
                        }
                }
            }
        }

        builder.setNeutralButton("Sign In") { dialog, which ->
            val email = emailInputField.text.toString().trim()
            val password = passwordInputField.text.toString().trim()
            when {
                email.isEmpty() ->
                    //inputField.error = "No word"
                    Snackbar.make(binding.root, "No email", Snackbar.LENGTH_LONG).show()
                password.isEmpty() -> Snackbar.make(
                    binding.root,
                    "No password",
                    Snackbar.LENGTH_LONG
                )
                    .show()
                else -> {
                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Snackbar.make(binding.root, "Signed in", Snackbar.LENGTH_LONG)
                                .show()
                        } else {
                            Snackbar.make(binding.root, "Could not sign in", Snackbar.LENGTH_LONG)
                                .show()
                        }
                    }
                }
            }
        }
        builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
        builder.show()
    }

    private fun showDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Add Item")

        val layout = LinearLayout(this@MainActivity)
        layout.orientation = LinearLayout.VERTICAL

        val titleInputField = EditText(this)
        titleInputField.hint = "Title"
        titleInputField.inputType = InputType.TYPE_CLASS_TEXT
        layout.addView(titleInputField)

        val descriptionInputField = EditText(this)
        descriptionInputField.hint = "Description"
        descriptionInputField.inputType = InputType.TYPE_CLASS_TEXT
        layout.addView(descriptionInputField)

        val bodyInputField = EditText(this)
        bodyInputField.hint = "Price"
        bodyInputField.inputType =
            InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        layout.addView(bodyInputField)

        val sellerInputField = EditText(this)
        sellerInputField.hint = "Seller"
        sellerInputField.inputType = InputType.TYPE_CLASS_TEXT
        layout.addView(sellerInputField)

        val dateInputField = EditText(this)
        dateInputField.hint = "Date"
        dateInputField.inputType = InputType.TYPE_CLASS_NUMBER
        layout.addView(dateInputField)

        val pictureInputField = EditText(this)
        pictureInputField.hint = "Picture URL"
        pictureInputField.inputType = InputType.TYPE_CLASS_TEXT
        layout.addView(pictureInputField)

        builder.setView(layout)

        builder.setPositiveButton("OK") { dialog, which ->
            val title = titleInputField.text.toString().trim()
            val description = descriptionInputField.text.toString().trim()
            val priceStr = bodyInputField.text.toString().trim()
            val seller = sellerInputField.text.toString().trim()
            val dateStr = dateInputField.text.toString().trim()
            val pictureUrl = pictureInputField.text.toString().trim()
            when {
                title.isEmpty() ->
                    //inputField.error = "No word"
                    Snackbar.make(binding.root, "No title", Snackbar.LENGTH_LONG).show()
                title.isEmpty() -> Snackbar.make(binding.root, "No title", Snackbar.LENGTH_LONG)
                    .show()
                priceStr.isEmpty() -> Snackbar.make(
                    binding.root,
                    "No price",
                    Snackbar.LENGTH_LONG
                )
                    .show()
                else -> {
                    val resaleItem = ResaleItem(
                        title,
                        description,
                        priceStr.toInt(),
                        seller,
                        dateStr.toInt(),
                        pictureUrl
                    )
                    resaleItemsViewModel.add(resaleItem)
                }
            }
        }
        builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
        builder.show()
    }

    private fun showFilterDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Filter items")

        val layout = LinearLayout(this@MainActivity)
        layout.orientation = LinearLayout.VERTICAL

        val minInputField = EditText(this)
        minInputField.hint = "Min. price"
        minInputField.inputType = InputType.TYPE_CLASS_NUMBER
        layout.addView(minInputField)

        val maxInputField = EditText(this)
        maxInputField.hint = "Max. price"
        maxInputField.inputType = InputType.TYPE_CLASS_NUMBER
        layout.addView(maxInputField)

        builder.setView(layout)

        builder.setPositiveButton("OK") { dialog, which ->
            val minPriceStr = minInputField.text.toString().trim()
            val maxPriceStr = maxInputField.text.toString().trim()
            when {
                minPriceStr.isEmpty() ->
                    //inputField.error = "No word"
                    Snackbar.make(binding.root, "No min. price", Snackbar.LENGTH_LONG).show()
                maxPriceStr.isEmpty() -> Snackbar.make(
                    binding.root,
                    "No max. price",
                    Snackbar.LENGTH_LONG
                ).show()
                else -> {
                    val minPrice = minPriceStr.toInt()
                    val maxPrice = maxPriceStr.toInt()
                    resaleItemsViewModel.filterByPrice(minPrice, maxPrice)
                }
            }
        }
        builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
        builder.show()
    }
}