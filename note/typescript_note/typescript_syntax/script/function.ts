//declare
function sayHello1(name: string): string {
    return 'Hello, ' + name;
}

var sayHello2 = function(name: string): string {
    return 'Hello, ' + name;
};

var sayHello3: (name: string) => string = function(name: string): string{
    return 'Hello, ' + name;
};

//optional parameters
function greetMe1(name: string, greeting?: string): string {
    if (!greeting) {
        greeting = 'Hello';
    }
    return greeting + ', ' + name;
}

//default parameters
function greetMe2(name: string, greeting: string = 'Hello'): string {
    return greeting + ', ' + name;
}

//rest parameters
function greetPeople(greeting: string, ...names: string[]): string {
    return greeting + ', ' + names.join(' and ') + '!';
}

alert(greetPeople('Hello', 'John', 'Ann', 'Fred'));

//overload
function hello(name: string): string;
function hello(names: string[]): string;
function hello(names: any, greeting?: string): string {
    var namesArray: string[];

    if (Array.isArray(names)) {
        namesArray = names;
    } else {
        namesArray = [names];
    }

    if (!greeting) {
        greeting = 'Hello';
    }

    return greeting + ', ' + namesArray.join(' and ') + '!';
}

//lambdas
var double = x => x * 2;
var add = (x, y) => x + y;
var reducedArray = [23, 5, 62, 16].reduce((a, b) => a + b, 0);
var addAndDouble = (x, y) => {
    var sum = x + y;
    return sum * 2;
};
function delayedGreeting(name): void {
    this.name = name;
    this.greet = function() {
        setTimeout(() => alert('Hello ' + this.name), 0);
    }
}

