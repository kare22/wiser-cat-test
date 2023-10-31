export function dateToString(date: Date): string {
    const options = { year: 'numeric', month: 'short', day: 'numeric' };
    // @ts-ignore
    return new Intl.DateTimeFormat('en-US', options).format(date);
}