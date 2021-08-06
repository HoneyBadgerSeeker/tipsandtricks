// From https://stackoverflow.com/a/57280361/10691359

type Comparator<T> = (a: T, b: T) => number; // -1 | 0 | 1
type Comparable = string | number;
  
/**
 * Use the function {@param evaluationFunc} on each element to get elements to compare and compare them
 * @param evaluationFunc apply it to the current element in order to get the value to compare
 * @param reversed       true if reversed order is wanted
 * @return comparison value (1, 0, -1)
 */
export function lambdaComparator<T>(evaluationFunc: ((item: T) => Comparable), reversed = false): Comparator<T> {
    return (a: T, b: T) => {
        const valA = evaluationFunc(a);
        const valB = evaluationFunc(b);
        let order = 0;
        if (valA < valB) {
            order = -1;
        } else if (valA > valB) {
            order = 1;
        }
        return reversed ? -order : order;
    };
}
  
/**
 * Call successively each comparators. Next comparators are called if there is an equality before
 * @param comparators list to call successively la liste des comparateurs à enchainer
 */
export function chainedComparator<T>(...comparators: Comparator<T>[]): Comparator<T> {
    return (a: T, b: T) => {
        let order = 0;
        let i = 0;
        while (!order && comparators[i]) {
            order = comparators[i++](a, b);
        }
        return order;
    };
}
