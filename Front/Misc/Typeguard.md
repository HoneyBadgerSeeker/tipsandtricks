https://stackoverflow.com/questions/69195889/typeguard-function-for-a-key-in-a-generic-object

function has<T extends object, K extends PropertyKey>(obj: T, property: K): obj is Extract<T, { [P in K]?: any }> {
    return Object.prototype.hasOwnProperty.call(obj, property)
}

// Then in user land somewhere:

interface Foo {
    bar: string
}

interface Fuzz {
    buzz: string
}

function doWork(thing: Foo | Fuzz) {
    if (has(thing, 'bar')) {
        alert(thing.bar);
        console.log('ttt');
    }
}
