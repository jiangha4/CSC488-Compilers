% CSC488 - Compilers

% A1.a A program that uses all arithmetic, logical, and comparison operators

begin
	
	% Init variables
	integer A <= 1,
	integer B <= 2,
	boolean T <= true,
	boolean F <= false,

	% Arithmetic Statements

	put A + B, skip,
	put B - A, skip,
	put A * B, skip,
	put A / B, skip,

	% Logical Statements

	if !T then put A, skip else put B, skip end,  
	if T & F then put A, skip else put B, skip end,
	if T | F then put A, skip else put B, skip end,

	% Comparison Operators

	if A = B then put A, skip end,
	if A != B then put A, skip end,
	if A < B then put A, skip end,
	if A <= B then put A, skip end,
	if A > B then put A, skip end,
	if A >= B then put A, skip end,

end