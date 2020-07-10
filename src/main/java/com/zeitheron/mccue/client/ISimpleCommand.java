package com.zeitheron.mccue.client;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

@FunctionalInterface
public interface ISimpleCommand
{
	static CommandBase create(final int permLvl, final String name, final ISimpleCommand exec)
	{
		return new CommandBase()
		{
			@Override
			public String getUsage(ICommandSender sender)
			{
				return name;
			}

			@Override
			public String getName()
			{
				return name;
			}

			@Override
			public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
			{
				exec.run(server, sender, args);
			}

			@Override
			public int getRequiredPermissionLevel()
			{
				return permLvl;
			}
		};
	}

	void run(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException;
}