import com.hirshi001.betternetworkingutil.CreateByteBufSerializer;

@CreateByteBufSerializer
public class ExampleClass {

    public static int doNotSerialize;

    public DependencyClass[][][] dotdot;
    public ExampleClass(int id, String name) {
    }

    public ExampleClass() {

    }
}
