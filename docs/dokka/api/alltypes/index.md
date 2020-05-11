

## Contents

### All Types

| Name | Summary |
|---|---|
|

##### [autoreset.api.LazyResetDelegateAbstractException](../autoreset.api/-lazy-reset-delegate-abstract-exception/index.md)

Abstract classes cannot be used with a 'by resets' delegate since they cannot be instantiated.


|

##### [autoreset.api.LazyResetDelegateException](../autoreset.api/-lazy-reset-delegate-exception/index.md)


|

##### [autoreset.api.LazyResetDelegateInterfaceException](../autoreset.api/-lazy-reset-delegate-interface-exception/index.md)

Interfaces cannot be used with a 'by resetss' delegate since they cannot be instantiated.


|

##### [autoreset.api.LazyResetDelegateNonDefaultConstructorException](../autoreset.api/-lazy-reset-delegate-non-default-constructor-exception/index.md)

Classes without a default constructor cannot be used with a 'by resets' delegate.


|

##### [autoreset.api.LazyResetDelegateObjectException](../autoreset.api/-lazy-reset-delegate-object-exception/index.md)

Objects may not be used with a 'by resets' delegate unless they implement the LazyReset interface.


|

##### [autoreset.api.LazyResets](../autoreset.api/-lazy-resets/index.md)

Lazy instance which can be reset.  After a reset, the next access will create a new instance.


|

##### [autoreset.api.ResetManager](../autoreset.api/-reset-manager/index.md)

Marks a class which is capable of tracking and resetting multiple [Resets](../autoreset.api/-resets/index.md) instances.


|

##### [autoreset.api.Resets](../autoreset.api/-resets/index.md)

Marks any type which may be reset.


