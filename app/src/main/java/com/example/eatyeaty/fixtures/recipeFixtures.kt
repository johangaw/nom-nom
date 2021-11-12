package com.example.eatyeaty.fixtures

import com.example.eatyeaty.repositories.ImageModel
import com.example.eatyeaty.repositories.Recipe


internal val recipe1: Recipe =
    Recipe(
        title = "Salty caramel cheesecake på kladdkakebotten",
        ingredients = listOf(
            "Kladdkakabotten",
            "2 ägg",
            "1 dl strösocker",
            "100 g smör",
            "1 dl vetemjöl",
            "0,75 dl kakao (av bra kvalitet)",
            "1 msk vaniljsocker",
        ),
        instructions = listOf(
            "Kladdkaka",
            "Smöra kanterna på en rund springform som är 24 cm i diameter med smör och lägg bakplåtspapper i botten. Smält smöret i en kastrull och låt svalna något. Rör ihop ägg och socker. Blanda ihop vetemjöl kakao och vaniljsocker. Sikta ner i äggsmeten och blanda. Häll i det smälta smöret och rör runt med en slickepott till en fin smet. Häll smeten i springformen och försök att fördela så jämnt som möjligt. Lägg åt sidan.",
            "",
            "Salty caramel",
            "Värm sockret på med medelhög värme i en kastrull. Rör om hela tiden tills allt socker har smält (sockret kommer hårdna och bli till små kristaller men den kommer att smälta, se bara till att sockret ej bränns). Tillsätt smöret och låt allt gå ihop. Häll i grädden och låt koka under omrörning i ca 1-2 minuter eller tills härligt seg rinnande och fin färg. Tillsätt salt och rör om. Låt svalna en stund."
        ),
        url = "https://www.koket.se/salty-caramel-cheesecake-pa-kladdkakebotten"
    )

internal val recipe2 = Recipe(
    url = "https://www.arla.se/recept/kladdkaka/?gclid=EAIaIQobChMImPCZ-P708wIVjgyRCh25vgRkEAAYAiAAEgL8R_D_BwE&gclsrc=aw.ds",
    title = "Kladdkaka",
    instructions = listOf(
        "Sätt ugnen på 175°.",
        "Smält smöret i en kastrull. Lyft av kastrullen från plattan.",
        "Rör ner socker och ägg, blanda väl. Rör ner övriga ingredienser så att allt blir väl blandat.",
        "Häll smeten i en smord och bröad form med löstagbar kant, ca 24 cm i diameter.",
        "Grädda mitt i ugnen ca 15 min. Kakan blir låg med ganska hård yta och lite kladdig i mitten.",
        "Låt kakan kallna. Pudra över florsocker. Servera med grädde eller glass och frukt.",
    ),
    ingredients = listOf(
        "Arla® Svenskt Smör 100 g",
        "Strösocker 2½ dl",
        "Ägg 2",
        "Vetemjöl 1 dl",
        "Kakao 3 msk",
        "Vaniljsocker 1 tsk",
    )
)


