import com.hirshi001.serializer.CreateByteBufSerializer;

import java.util.concurrent.ThreadLocalRandom;

@CreateByteBufSerializer
public class DependencyClass {

    public int dependency = ThreadLocalRandom.current().nextInt();

    @Override
    public String toString() {
        return Integer.toString(dependency);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof DependencyClass && ((DependencyClass) obj).dependency == dependency;
    }
}
