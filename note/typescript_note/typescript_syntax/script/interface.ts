//interface
interface Vehicle {
    make: string;
}
class Car implements Vehicle {
    // Compiler will raise a warning if this property is not declared
    make: string;
    constructor(make: string) {};
}

//extend
class Sedan extends Car {
    model: string;
    constructor(make: string, model: string) {
        super(make);
        this.model = model;
    }
}

//implement
interface IException {
    message: string;
    id?: number;
}

interface IExceptionArrayItem {
    [index: number]: IException;
}

interface IErrorHandler {
    exceptions: IExceptionArrayItem[];
    logException(message: string, id?: number): void;
}

interface IExceptionHandlerSettings {
    logAllExceptions: boolean;
}

class ErrorHandler implements IErrorHandler {
    exceptions: IExceptionArrayItem[];
    logAllExceptions: boolean;

    constructor(settings: IExceptionHandlerSettings) {
        this.logAllExceptions = settings.logAllExceptions;
    }

    logException(message: string, id?: number): void {
        this.exceptions.push({ message, id });
    }
}
