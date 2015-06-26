package vazkii.waterproof;

import java.util.Iterator;

import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class ClassTransformer implements IClassTransformer {

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		if(transformedName.equals("net.minecraft.block.BlockDynamicLiquid")) {
			log("Starting on " + transformedName);
			String funcName = "func_149807_p";
			String obfName = "p";
			String funcDesc = "(Lnet/minecraft/world/World;III)Z";
			String obfDesc = "(Lahb;III)Z";
			
			log("Method is " + funcName + " or " + obfName + " for obf.");
			log("Descriptor is " + funcDesc + " or " + obfDesc + " dor obf.");
			
			ClassReader reader = new ClassReader(basicClass);
			ClassNode node = new ClassNode();
			reader.accept(node, 0);

			for(MethodNode method : node.methods)
				if((method.name.equals(funcName)|| method.name.equals(obfName)) && (method.desc.equals(funcDesc) || method.desc.equals(obfDesc))) {
					log("Found method: " + method.name + " " + method.desc);
					Iterator<AbstractInsnNode> iterator = method.instructions.iterator();

					while(iterator.hasNext()) {
						AbstractInsnNode anode = (AbstractInsnNode) iterator.next();
						InsnList newInstructions = new InsnList();

						newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
						newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
						newInstructions.add(new VarInsnNode(Opcodes.ILOAD, 2));
						newInstructions.add(new VarInsnNode(Opcodes.ILOAD, 3));
						newInstructions.add(new VarInsnNode(Opcodes.ILOAD, 4));
						newInstructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "vazkii/waterproof/Waterproof", "isWaterproof", "(Lnet/minecraft/world/World;III)Z"));
						LabelNode label = new LabelNode();
						newInstructions.add(new JumpInsnNode(Opcodes.IFEQ, label));
						newInstructions.add(new InsnNode(Opcodes.ICONST_1));
						newInstructions.add(new InsnNode(Opcodes.IRETURN));
						newInstructions.add(label);

						method.instructions.insertBefore(anode, newInstructions);
						log("Patched!");
						break;
					}

					ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
					node.accept(writer);
					return writer.toByteArray();
				}
		}

		return basicClass;
	}

	private static void log(String str) {
		System.out.println("[Waterproof][ASM] " + str);
	}

}
