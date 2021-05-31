package org.zeith.mccue;

import com.zeitheron.hammercore.utils.recipes.helper.RecipeRegistry;
import com.zeitheron.hammercore.utils.recipes.helper.RegisterRecipes;

@RegisterRecipes(modid = "mccue")
public class RecipesMcCue
		extends RecipeRegistry
{
	public void crafting()
	{
		shaped(McCue.REDSTONE_TRIGGER,
				"grg",
				"rcr",
				"grg",
				Character.valueOf('g'),
				"nuggetGold",
				Character.valueOf('r'),
				"dustRedstone",
				Character.valueOf('c'),
				"cobblestone");
	}
}