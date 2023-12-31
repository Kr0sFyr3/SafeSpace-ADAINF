package nl.paulkros.safespace.screens

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import nl.paulkros.safespace.R
import nl.paulkros.safespace.classes.MunicipalityItem
import nl.paulkros.safespace.classes.Score
import okhttp3.internal.format
import org.w3c.dom.Text

class ScoreFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_score, container, false)

        //We find all the Views and the Layout and asssign them to a variable
        val backButton = view.findViewById<ImageView>(R.id.backButton)
        val gemeente = view.findViewById<TextView>(R.id.locationName)
        val score = view.findViewById<TextView>(R.id.score)
        val layout = view.findViewById<ConstraintLayout>(R.id.scoreLayout)
        val logo = view.findViewById<ImageView>(R.id.scoreLogo)

        //When the backbutton is pressed we start a transaction in the fragmentmanager to head towards the LocationFragment
        backButton.setOnClickListener{
            val fragment = LocationFragment()
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, fragment)
            transaction.commit()
        }

        //We get the municipality name value that was given passed along from the LocationFragment and then change the text property of gemeente to the passed value.
        //If something wrent wrong with the data passing we display an error
        val municipality = arguments?.getSerializable("municipality") as? MunicipalityItem
        if (municipality != null) {
            gemeente.setText(municipality.gemeente)
        } else {
            gemeente.setText("Er is iets fout gegaan")
        }
        var latestScore: Score? = null
        if (municipality != null) {
            for (score in municipality.score) {
                if (latestScore == null || score.datum > latestScore.datum) {
                    latestScore = score
                }
            }
        }

        //We get the score value that was given passed along from the LocationFragment
        //We loop through the scores stored inside the MunicipalityItem to find the latest one
        //Then we check whether  the score falls in a certain range and change the gradient and logo accordingly
        if (latestScore != null) {
            val formattedscore = latestScore.veiligheidsScore.toInt()
            if (formattedscore == 0){
                layout.setBackgroundResource(R.drawable.gradient_black)
                logo.setImageResource(R.drawable.scorenegativelogo)
            }
            else if (formattedscore in 1..25){
                layout.setBackgroundResource(R.drawable.gradient_red)
                logo.setImageResource(R.drawable.scorenegativelogo)
            }
            else if (formattedscore in 25..50){
                layout.setBackgroundResource(R.drawable.gradient_orange)
                logo.setImageResource(R.drawable.scorenegativelogo)
            }
            else if (formattedscore in 50..75){
                layout.setBackgroundResource(R.drawable.gradient_yellow)
                logo.setImageResource(R.drawable.scorepositivelogo)
            }
            else if (formattedscore in 50..75){
                layout.setBackgroundResource(R.drawable.gradient_green)
                logo.setImageResource(R.drawable.scorepositivelogo)
            }
            score.setText(formattedscore.toString())
            Log.d("LatestScore", latestScore.toString())
        } else {
            score.setText("?")
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ScoreFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}