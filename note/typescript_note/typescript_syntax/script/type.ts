//string
var brand: string = 'Chevrolet';
var message: string = `Today it's a happy day! I just bought a new ${brand} car`;

//number
var age: number = 7;
var height: number = 5.6;

//boolean
var isZero: boolean = false;

//array
var brands: string[] = ['Chevrolet', 'Ford', 'General Motors'];
var childrenAges: number[] = [8, 5, 12, 3, 1];

//any
var distance: any;
distance ='1000km';
distance = 1000;

var distances: any[] = ['1000km', 1000];

//enum
enum Brands { Chevrolet, Cadillac, Ford, Buick, Chrysler, Dodge };
var myCar: Brands = Brands.Cadillac;
var myCarBrandName: string = Brands[1];

enum BrandsReduced { Tesla = 1, GMC, Jeep };
var myTruck: BrandsReduced = BrandsReduced.GMC;

enum StackingIndex {
    None = 0,
    Dropdown = 1000,
    Overlay = 2000,
    Modal = 3000
};
var mySelectBoxStacking: StackingIndex = StackingIndex.Dropdown;

//void
function resetPomodoro(): void {
    this.minutes = 24;
    this.seconds = 59;
}
