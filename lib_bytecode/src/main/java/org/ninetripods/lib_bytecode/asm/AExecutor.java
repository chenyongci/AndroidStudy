package org.ninetripods.lib_bytecode.asm;

import org.ninetripods.lib_bytecode.BConstant;
import org.ninetripods.lib_bytecode.MethodTimeCostTest;
import org.ninetripods.lib_bytecode.util.Loader;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.io.IOException;

import static org.ninetripods.lib_bytecode.BConstantKt.log;
import static org.ninetripods.lib_bytecode.asm.AddTimeCostVisitorKt.FIELD_NAME_ADD;

public class AExecutor {

    public static void main(String[] args) {
        try {
            ClassReader classReader = new ClassReader(MethodTimeCostTest.class.getName());
            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            ClassVisitor classVisitor = new AddTimeCostVisitor(BConstant.ASM9, classWriter);
            classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES);

            Loader loader = new Loader();
            try {
                Class addTimeClass = loader.defineClass(MethodTimeCostTest.class.getName(), classWriter.toByteArray());
                Object instance = addTimeClass.newInstance();
                addTimeClass.getDeclaredMethod("addTimeCostMonitor").invoke(addTimeClass);
                Long timeCost = addTimeClass.getDeclaredField(FIELD_NAME_ADD).getLong(instance);
                log("timeCost:" + timeCost);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
