package com.playing.cardfun

import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private var cards = ArrayList<Card>()
    private val player1Cards = ArrayList<Card>()
    private val player2Cards = ArrayList<Card>()
    lateinit var grayCard: ImageView
    private var player1Played: Boolean = false
    private var player2Played: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        grayCard = ImageView(applicationContext)

        distributeCards()

        player1card0.setOnClickListener{ player1CardClicked(player1card0, R.string.p1card) }
        player1card1.setOnClickListener{ player1CardClicked(player1card1, R.string.p1card) }
        player1card2.setOnClickListener{ player1CardClicked(player1card2, R.string.p1card) }
        player1card3.setOnClickListener{ player1CardClicked(player1card3, R.string.p1card) }
        player1card4.setOnClickListener{ player1CardClicked(player1card4, R.string.p1card) }

        player2card0.setOnClickListener{ player2CardClicked(player2card0, R.string.p2card) }
        player2card1.setOnClickListener{ player2CardClicked(player2card1, R.string.p2card) }
        player2card2.setOnClickListener{ player2CardClicked(player2card2, R.string.p2card) }
        player2card3.setOnClickListener{ player2CardClicked(player2card3, R.string.p2card) }
        player2card4.setOnClickListener{ player2CardClicked(player2card4, R.string.p2card) }

        deck.setOnClickListener {
            println("deck clicked $player1Played  $player2Played")
            cards.shuffle()
            val newCard = cards.get(0)
            if(player1Played) {
                grayCard.setImageResource(newCard.imageId)
                grayCard.setTag(R.string.p1card, newCard)
                player1Cards.add(newCard)
                player1Played = false
                cards.remove(newCard)
                setPlayerEnabled("player1", false)
                setPlayerEnabled("player2", true)
            } else if (player2Played) {
                grayCard.setImageResource(newCard.imageId)
                grayCard.setTag(R.string.p2card, newCard)
                player2Cards.add(newCard)
                player2Played = false
                cards.remove(newCard)
                setPlayerEnabled("player2", false)
                setPlayerEnabled("player1", true)
            }
        }

        show_btn.setOnClickListener{
            println(player1Cards.size)
            println(player2Cards.size)

            val t = Toast.makeText(applicationContext, " Points player1 = " + player1Cards.sumBy{ card -> card.points } + " player2 = " + player2Cards.sumBy { card -> card.points }, Toast.LENGTH_LONG)
            t.show()
        }

        usedCardPlayer1.setOnClickListener {
            val newCard: Card = usedCardPlayer1.getTag(R.string.p1card) as Card

            if (player2Played) {
                grayCard.setImageResource(newCard.imageId)
                grayCard.setTag(R.string.p2card, newCard)
                player2Cards.add(newCard)
                usedCardPlayer1.setImageResource(R.drawable.red_back)
                usedCardPlayer1.setTag(R.string.p1card, null)
                player2Played = false
                setPlayerEnabled("player2", false)
                setPlayerEnabled("player1", true)
            }
        }

        usedCardPlayer2.setOnClickListener {
            val newCard: Card = usedCardPlayer2.getTag(R.string.p2card) as Card

            if(player1Played) {
                grayCard.setImageResource(newCard.imageId)
                grayCard.setTag(R.string.p1card, newCard)
                player1Cards.add(newCard)
                usedCardPlayer2.setImageResource(R.drawable.red_back)
                usedCardPlayer2.setTag(R.string.p2card, null)
                player1Played = false
                setPlayerEnabled("player1", false)
                setPlayerEnabled("player2", true)
            }
        }
    }

    private fun usedCardClick(cardImage: ImageView, card: Card, tag: Int) {
        grayCard.setImageResource(card.imageId)
        grayCard.setTag(tag, card)

        cardImage.setImageResource(R.drawable.red_back)
        cardImage.setTag(R.string.p1card, null)
    }

    private fun setPlayerEnabled(playerStr: String, isEnabled: Boolean) {
        for (x in 0.. 4) {
            var playerCardStr = playerStr+"card$x"
            var playerVar: ImageView =
                findViewById(resources.getIdentifier(playerCardStr, "id", packageName))
            playerVar.isEnabled = isEnabled
        }
    }

    private fun player1CardClicked(playerCard: ImageView, tag: Int) {
        println("player1CardClicked" + playerCard.getTag(tag) + " " + player1Played)
        println("gray back tag " + R.drawable.gray_back)
        if(playerCard.getTag(tag) != R.drawable.gray_back && !player1Played) {
            player2Played = false
            player1Played = true
            player1Cards.remove(playerCard.getTag(tag))

            handlePlayerCardClick(playerCard, tag, usedCardPlayer1)
        }
    }

    private fun player2CardClicked(playerCard: ImageView, tag: Int) {
        println("player2CardClicked" + playerCard.getTag(tag) + " " + player1Played)
        println("gray back tag " + R.drawable.gray_back)
        if(playerCard.getTag(tag) != R.drawable.gray_back && !player2Played) {
            player1Played = false
            player2Played = true
            player2Cards.remove(playerCard.getTag(tag))

            handlePlayerCardClick(playerCard, tag, usedCardPlayer2)
        }
    }

    private fun handlePlayerCardClick(playerCard: ImageView, tag: Int, usedCard: ImageView) {

        grayCard = playerCard
        usedCard.setImageDrawable(playerCard.drawable)
        usedCard.setTag(tag, playerCard.getTag(tag) as Card)

        playerCard.setImageResource(R.drawable.gray_back)
        playerCard.setTag(tag, R.drawable.gray_back)
        grayCard.setTag(tag, R.drawable.gray_back)

    }

    private fun createCardList() : ArrayList<Card> {
        val cardList = ArrayList<Card>()

        cardList.add(Card("club_ace", 1, R.drawable.club_ace))
        cardList.add(Card("diamond_ace",1, R.drawable.diamond_ace))
        cardList.add(Card("heart_ace", 1, R.drawable.heart_ace))
        cardList.add(Card("spade_ace", 1, R.drawable.spade_ace))

        cardList.add(Card("club_king", 10, R.drawable.club_king))
        cardList.add(Card("diamond_king", 10, R.drawable.diamond_king))
        cardList.add(Card("heart_king", 10, R.drawable.heart_king))
        cardList.add(Card("spade_king", 10, R.drawable.spade_king))

        cardList.add(Card("club_queen", 10, R.drawable.club_queen))
        cardList.add(Card("diamond_queen", 10, R.drawable.diamond_queen))
        cardList.add(Card("heart_queen", 10, R.drawable.heart_queen))
        cardList.add(Card("spade_queen", 10, R.drawable.spade_queen))

        cardList.add(Card("club_jack", 10, R.drawable.club_jack))
        cardList.add(Card("diamond_jack", 10, R.drawable.diamond_jack))
        cardList.add(Card("heart_jack", 10, R.drawable.heart_jack))
        cardList.add(Card("spade_jack", 10, R.drawable.spade_jack))

        cardList.add(Card("club_10", 10, R.drawable.club10))
        cardList.add(Card("diamond_10", 10, R.drawable.diamond10))
        cardList.add(Card("heart_10", 10, R.drawable.heart10))
        cardList.add(Card("spade_10", 10, R.drawable.spade10))

        cardList.add(Card("club_9", 9, R.drawable.club9))
        cardList.add(Card("diamond_9", 9, R.drawable.diamond9))
        cardList.add(Card("heart_9", 9, R.drawable.heart9))
        cardList.add(Card("spade_9", 9, R.drawable.spade9))

        cardList.add(Card("club_8", 8, R.drawable.club8))
        cardList.add(Card("diamond_8", 8, R.drawable.diamond8))
        cardList.add(Card("heart_8", 8, R.drawable.heart8))
        cardList.add(Card("spade_8", 8, R.drawable.spade8))

        cardList.add(Card("club_7", 7, R.drawable.club7))
        cardList.add(Card("diamond_7", 7, R.drawable.diamond7))
        cardList.add(Card("heart_7", 7, R.drawable.heart7))
        cardList.add(Card("spade_7", 7, R.drawable.spade7))


        cardList.add(Card("club_6", 6, R.drawable.club6))
        cardList.add(Card("diamond_6", 6, R.drawable.diamond6))
        cardList.add(Card("heart_6", 6, R.drawable.heart6))
        cardList.add(Card("spade_6", 6, R.drawable.spade6))

        cardList.add(Card("club_6", 6, R.drawable.club6))
        cardList.add(Card("diamond_6", 6, R.drawable.diamond6))
        cardList.add(Card("heart_6", 6, R.drawable.heart6))
        cardList.add(Card("spade_6", 6, R.drawable.spade6))

        cardList.add(Card("club_5", 5, R.drawable.club5))
        cardList.add(Card("diamond_5", 5, R.drawable.diamond5))
        cardList.add(Card("heart_5", 5, R.drawable.heart5))
        cardList.add(Card("spade_5", 5, R.drawable.spade5))

        cardList.add(Card("club_4", 4, R.drawable.club4))
        cardList.add(Card("diamond_4", 4, R.drawable.diamond4))
        cardList.add(Card("heart_4", 4, R.drawable.heart4))
        cardList.add(Card("spade_4", 4, R.drawable.spade4))

        cardList.add(Card("club_3", 3, R.drawable.club3))
        cardList.add(Card("diamond_3", 3, R.drawable.diamond3))
        cardList.add(Card("heart_3", 3, R.drawable.heart3))
        cardList.add(Card("spade_3", 3, R.drawable.spade3))

        cardList.add(Card("club_2", 2, R.drawable.club2))
        cardList.add(Card("diamond_2", 2, R.drawable.diamond2))
        cardList.add(Card("heart_2", 2, R.drawable.heart2))
        cardList.add(Card("spade_2", 2, R.drawable.spade2))

        cardList.shuffle()

        return cardList
    }

    private fun distributeCards() {
        shuffle_btn.setOnClickListener{
            // Reset everything
            cards = createCardList()
            player1Cards.clear()
            player2Cards.clear()
            usedCardPlayer1.setImageResource(R.drawable.red_back)
            usedCardPlayer1.setTag(R.string.p2card, null)
            usedCardPlayer2.setImageResource(R.drawable.red_back)
            usedCardPlayer2.setTag(R.string.p2card, null)
            player1Played = false
            player2Played = false

            cards.shuffle();

            val usedCards = ArrayList<Card>()
            usedCards.addAll(listOf(cards[0], cards[1], cards[2], cards[3], cards[4], cards[5], cards[6], cards[7], cards[8], cards[9]))
            player1Cards.addAll(listOf(cards[0], cards[1], cards[2], cards[3], cards[4]))
            player2Cards.addAll(listOf(cards[5], cards[6], cards[7], cards[8], cards[9]))

            player1card0.setImageResource(cards[0].imageId)
            player1card0.setTag(R.string.p1card, cards[0])
            player1card1.setImageResource(cards[1].imageId)
            player1card1.setTag(R.string.p1card, cards[1])
            player1card2.setImageResource(cards[2].imageId)
            player1card2.setTag(R.string.p1card, cards[3])
            player1card3.setImageResource(cards[3].imageId)
            player1card3.setTag(R.string.p1card, cards[3])
            player1card4.setImageResource(cards[4].imageId)
            player1card4.setTag(R.string.p1card, cards[4])

            player2card0.setImageResource(cards[5].imageId)
            player2card0.setTag(R.string.p2card, cards[5])
            player2card1.setImageResource(cards[6].imageId)
            player2card1.setTag(R.string.p2card, cards[6])
            player2card2.setImageResource(cards[7].imageId)
            player2card2.setTag(R.string.p2card, cards[7])
            player2card3.setImageResource(cards[8].imageId)
            player2card3.setTag(R.string.p2card, cards[8])
            player2card4.setImageResource(cards[9].imageId)
            player2card4.setTag(R.string.p2card, cards[9])

            cards.removeAll(usedCards)
        }
    }
}
